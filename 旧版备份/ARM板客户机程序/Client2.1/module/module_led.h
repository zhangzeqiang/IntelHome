#ifndef __MODULE_LED_H__
#define __MODULE_LED_H__
#include "../include/myType.h"
#include "../include/protocol.h"

int ioctl_led(int fd, const struct dev_node dev[], size_t size);
int readInfo_led(int fd, struct dev_node dev[], size_t size);

int ioctl_led(int fd, const struct dev_node *dev, size_t size){
	TYPE8 state = 0;	
	if (size < 1)	return -1;
	state = dev[0].state;
	state = state%2;
	ioctl(fd, state, dev[0].devno);
	return 1;
}

int readInfo_led(int fd, struct dev_node *dev, size_t size){
	TYPE8 buff[2];
	int i = 0;
	if (size < 3)		return -1;
	if (read(fd, buff, 1) < 0)	return -2;
	for (i=0;i<4;i++){
		dev[i].state = ((1<<i) & buff[0])>>i;	//state
		dev[i].devno = i;			//devno
	}
	return 1;
}


#endif
