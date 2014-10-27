#ifndef __SERVER__H__
#define __SERVER__H__

#include <unistd.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <netdb.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <ctype.h>
#include <errno.h>
#include <malloc.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <sys/ioctl.h>
#include <stdarg.h>
#include <fcntl.h>
#include <fcntl.h>
#include <pthread.h>
#include "sockWindows.h"
#include <sys/ioctl.h>
#include <sys/select.h>
#include <sys/time.h>
#include "myType.h"
#include "../module/module_433.h"
#include "../module/module_led.h"

#define BACKLOG 10
/*
 *全局变量
 */
struct sockaddr_in server_servaddr;
static int listenfd;

/*
 * 函数声明
 */
void server_socket_init(unsigned short);
void server_socket_close();
void server_socket_body(unsigned short);

/*
 * 线程函数声明
 */
static void * doit(void *);
void server_socket_body(unsigned short port){
	pthread_t tid;
	server_socket_init(port);
	//int *iptr;
	//thread_t apid;
	//socklen_t addrlen,len;
	int connfd;
	printf("监听\n");
	//时刻监听socket，若有新连接，则新建线程进行处理.
	for (;;){
		connfd = accept(listenfd,(struct sockaddr*)NULL, NULL);
		//pthread_create(&apid,NULL,&doit,connfd);
		pthread_create(&tid, NULL, &doit, (void *)connfd);
		//close(connfd);
	}
	server_socket_close();
}
static void * doit(void *arg){
	//unsigned char rec[100];
	TYPE8 rec[100];
	TYPE8 buff[2];
	int i;

	read((int)arg, rec, 100);
	unsigned char len = rec[0];	//len
	unsigned char type = rec[1];	//type
	unsigned char id = rec[2];	//id
	unsigned char passwd = rec[3];	//passwd
	unsigned char dev_num = rec[4];	//dev_num
	unsigned char devno;	//devno1
	unsigned char state;	//state1

	//----------------------------------------------------
	// open device file --- TODO
	int led_fd = open(LED_FILE, O_RDWR);
	int fd_433 = open_433(_433_FILE2);

	if (led_fd < 0 || fd_433 < 0){
	//----------------------------------------------------
		perror("open device error\n");
                //exit(1);
		rec[0] = 3;
		rec[2] = 0;
		write((int)arg,rec, 100);
		goto close_file;
	}

	for (i=0;i<dev_num;i++){
		devno = rec[i*2+5];	//devno
		state = rec[i*2+6];	//dev_state
		//-------------------------------------------------
		//control device --- TODO
		switch(devno){		
		case LED0:
		case LED1:
		case LED2:
		case LED3:{
/*
			state = state%2;
			ioctl(led_fd, state, devno);	*/
			struct dev_node *dev = malloc(sizeof(struct dev_node));
			dev->state = state;
			dev->devno = devno;
			ioctl_led(led_fd, dev, 1);
			free(dev);
	
		}break;	
		case _433_DEV0:
		case _433_DEV1:
		case _433_DEV2:
		case _433_DEV3:{

			struct dev_node *dev = malloc(sizeof(struct dev_node));
			dev->state = state;
			dev->devno = devno;
			ioctl_433(fd_433, dev, 1);
			free(dev);

		}break;
		default:break;	
		} 
		//----------------------------------------------
	}

	write((int)arg,rec, 100);

close_file:
	//-------------------------------------------------------
	//close devices --- TODO
	close(led_fd);
	close(fd_433);

	//-------------------------------------------------------
	close((int)arg);
	return 0;
}
void server_socket_init(unsigned short port){
	char buff[100];
		
	// 建立socket 		
	listenfd = socket(AF_INET, SOCK_STREAM, 0);
	
	// 设置socket协议类型
	bzero(&server_servaddr, sizeof(server_servaddr));
	server_servaddr.sin_family = AF_INET;			//TCP	
	server_servaddr.sin_addr.s_addr = htonl(INADDR_ANY);   //IP
	server_servaddr.sin_port = htons(port);
	
	// bind端口
	bind(listenfd, (struct sockaddr*)&server_servaddr, sizeof(server_servaddr));

	// 监听端口
	listen(listenfd, BACKLOG);
}

void server_socket_close(){
	close(listenfd);
}



#endif
