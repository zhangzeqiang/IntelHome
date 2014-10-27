#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
int main(int arg, int argv[]){
	int fd;
	char buf[100];
	fd = open("/dev/LED0", O_RDWR);
	fd = open("/dev/LED0", O_RDWR);	
	if (fd != -1){
		printf("success to open /dev/LED\n");
		
		//read(fd, buf, 1);
	//	printf("%c\n", buf[0]);
		if (ioctl(fd, 0, 1)){
			printf("ioctl success\n");
		}
		read(fd, buf, 1);
		buf[0] = 0x07;
		if (write(fd, buf, 1) == -1){
			close(fd);
			close(fd);
			return 0;
		}
		read(fd, buf, 1);
		printf("read:%d\n", (int)buf[0]);
/*	
		if (ioctl(fd, 0, 2)){
			printf("ioctl success\n");
		}
		read(fd, buf, 1);
		printf("read:%d\n", (int)buf[0]);*/
		close(fd);
		close(fd);
	}else{
		printf("fail to open /dev/LED\n");
	}

	/*fd = open("/dev/LED2", O_RDWR);
	
	if (fd){
		printf("success to open /dev/LED\n");
		
		read(fd, buf, 1);
/*		printf("%c\n", buf[0]);
		if (ioctl(fd, 0, 0)){
			printf("ioctl success\n");
		}*/
	/*	close(fd);
	}else{
		printf("fail to open /dev/LED\n");
	}*/
	
	
	return 0;
}
