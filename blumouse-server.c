/*...........................................................|
|blumouse-server.c 											 |
|created by harshavardhan kode on 06/06/2012				 |
|...........................................................*/

#include <stdio.h>
#include <unistd.h>
#include <sys/socket.h>
#include <bluetooth/bluetooth.h>
#include <bluetooth/rfcomm.h>
#include<X11/Xlib.h>
#include<X11/Xutil.h>
#include <X11/extensions/XTest.h>
#include<stdlib.h>
#include <time.h>
#include <string.h>


void movecursor(int x ,int y);
void mouseClick(int button);
void mouseChange(int button);
void act( char* buf);

Display *dpy;
Window root_window;

int sensitivity = 2 , xres = 1366 , yres = 768;

int cur_x = 0 ,cur_y = 0 ,temp_posx,temp_posy,mtemp_x,mtemp_y;
int l_clicked = 0 ;
int r_clicked = 0;

int main(int argc, char **argv)
{	
	fprintf(stderr, "launch the app blumouse in your phone now! \n");
    struct sockaddr_rc loc_addr = { 0 }, rem_addr = { 0 };
    char buf[1024] = { 0 };
    int s, client, bytes_read,flag=1;
    socklen_t opt = sizeof(rem_addr);

    // allocate socket
    s = socket(AF_BLUETOOTH, SOCK_STREAM, BTPROTO_RFCOMM);

    // bind socket to port 1 of the first available 
    // local bluetooth adapter
    loc_addr.rc_family = AF_BLUETOOTH;
    loc_addr.rc_bdaddr = *BDADDR_ANY;
    loc_addr.rc_channel = (uint8_t) 1;
    bind(s, (struct sockaddr *)&loc_addr, sizeof(loc_addr));
	
    // put socket into listening mode
    listen(s, 1);

    // accept one connection
    client = accept(s, (struct sockaddr *)&rem_addr, &opt);

    ba2str( &rem_addr.rc_bdaddr, buf );
    fprintf(stderr, "accepted connection from %s\n", buf);
    memset(buf, 0, sizeof(buf));


	// initialise X components

	dpy = XOpenDisplay(0);
	root_window = XRootWindow(dpy, 0);
	XSelectInput(dpy, root_window, KeyReleaseMask);
    // read data from the client
	while(1){
    bytes_read = read(client, buf, sizeof(buf));
    if( bytes_read > 0 ) {
		//if(flag==1){
        printf("%s \n", buf);
		//flag=2;}
	switch(buf[0]){
		case 'l':
		mouseClick(1);
		break;
    	case 'r':
		mouseClick(3);
		break;
		case '(': 
		act(buf);
		break;
		case 'R':
		mouseChange(3);
		break;
		case 'L':
		mouseChange(1);
		break;
    	}}
	else { 
		}
	
	}
    // close connection
    close(client);
    close(s);
	
	//move(100,100);
	//refresh();


    return 0;
}

void act(char* buf){  // recursive function

int i,d1=25,d2=25,xpos, pos_isto=25, x = 0 ,y = 0 ,temp;

	for(i=0;i<24;i++)
	{	
		if(buf[i] == '.')
		{if(d1 < 24){d2 = i;}
		else {d1=i;} }
		if(buf[i] == 'x')
		xpos = i;
		if(buf[i] == ':' && pos_isto == 25)
		pos_isto = i;
	}

	

	for(i=1;i<d1;i++)
	{
	temp = buf[i];
	temp = temp - 48;
	x = x*10 + temp;
	}

	for(i=xpos+1;i<d2;i++)
	{
	temp = buf[i];
	temp = temp - 48;
	y = y*10 + temp;
	}
	temp = buf[pos_isto + 1];
	//printf("switch started with %d\n",temp);
	switch(temp)
	{case 48 :
		cur_x = mtemp_x;
		cur_y = mtemp_y;
		temp_posx = x; 
		//printf("%d = %d (tempx, x) " ,temp_posx ,x);
		temp_posy = y;
		movecursor(cur_x,cur_y);
		break;
	case 49:
		//printf("(tempx:%d, x:%d,curx:%d \n) " ,temp_posx ,x,cur_x);
		cur_x = mtemp_x;
		cur_y = mtemp_y;
		break;
	case 50 :
		temp = (x-temp_posx)*sensitivity+cur_x;
		if(temp < xres && temp > 0)
		mtemp_x = temp;
		temp = (y-temp_posy)*sensitivity+cur_y;
		if(temp < yres && temp > 0)
		mtemp_y = temp;
		movecursor(mtemp_x,mtemp_y);
		break;
		}

	
	//printf("xpos: %d ; d1: %d ; d2 : %d  ;x : %d ;y : %d ; isto_pos:%d ; cur_posx : %d , cur_posy : %d \n",xpos,d1,d2,x,y,pos_isto,cur_x,cur_y);
	//movecursor ( x, y);

	
	
}

	

void movecursor(int x ,int y)
{

int i;

// printf("\n moved \n");

XWarpPointer(dpy, None, root_window, 0, 0, 0, 0, x, y);
XFlush(dpy); // Flushes the output buffer, therefore updates the cursor's position. Thanks to Achernar.
}

void mouseChange(int button)
{
int temp;
if(button == 3){
r_clicked = 1 - r_clicked;
temp= r_clicked;
//XTestFakeButtonEvent(dpy, button, r_clicked, CurrentTime);
}

if(button == 1){
l_clicked = 1 - l_clicked;
temp= l_clicked;
//XTestFakeButtonEvent(dpy, button, l_clicked, CurrentTime);
}
if(temp == 1){
printf("button down!");
XTestFakeButtonEvent(dpy, button, True, CurrentTime);}
else
XTestFakeButtonEvent(dpy, button, False, CurrentTime);

XFlush(dpy);
}

void mouseClick(int button)
{
XTestFakeButtonEvent(dpy, button, True, CurrentTime);
XTestFakeButtonEvent(dpy, button, False, CurrentTime);
XFlush(dpy);

}


