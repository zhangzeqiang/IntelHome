#include "module433.h"
#include "protocol.h"
#include "myType.h"

#define NUMBER _433_DEV0

int main(void)
{
	int fd;
	struct info_node *node;
	node = malloc(sizeof(struct info_node));

	TYPE8 buf[3];
	char ch;
	/*
	 * open device file
 	 */
	int led_fd = open(LED_FILE, O_RDWR);
	fd = open_433(_433_FILE1);
	if (fd < 0 || led_fd < 0){
		close(fd);
		close(led_fd);
		exit(1);
		return 0;
	}
//	printf("ij");
	while(1)
	{	
		
		if (read_433(fd, node) < 0 || (node->dev_from == 0 && node->dev_to == 0 && node->dev_state == 0)){
			continue;	
		}
		
		printf("receive:%d ", node->dev_from);
		printf("%d ", node->dev_to);
		printf("%d \n", node->dev_state);	

		if (node->dev_to != NUMBER || node->dev_from != LOCAL){
			continue;
		}
		printf("accept:\n");

		TYPE8 devno = ((node->dev_state) & 0xf0)>>4;
		TYPE8 state = ((node->dev_state) & 0x0f);
		
		printf("devno:%d ", devno);
		printf("state:%d\n", state);
		
		switch (devno){
		case LED0:
		case LED1:
		case LED2:
		case LED3:{
			state = state%2;
			ioctl(led_fd, state, devno);	
		}break;
		default:break;
		}
	
		sleep(1);
	}
	free(node);
	close(fd);
	close(led_fd);
//	close(fd2);
	return 0;
}
