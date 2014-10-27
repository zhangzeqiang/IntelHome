#include "module433.h"
#include "protocol.h"

int main(void)
{
	int fd;
	struct info_node *node;
	node = malloc(sizeof(struct info_node));

	TYPE8 buf[3];
	char ch;
	fd = open_433("/dev/ttySAC2");
	if (fd < 0)
		exit(1);
	printf("ij");
	while(1)
	{	
		//printf("\n");
		node->dev_from = (TYPE8)32;
		node->dev_to = (TYPE8)245;
		node->dev_state = (TYPE8)21;
		
		printf("buf1:%d ", node->dev_from);
		printf("%d ", node->dev_to);
		printf("%d \n", node->dev_state);	

		//itn((TYPE8 *)buf, node);
		write_433(fd, node);
		sleep(1);
	}
	free(node);
	close(fd);
//	close(fd2);
	return 0;
}
