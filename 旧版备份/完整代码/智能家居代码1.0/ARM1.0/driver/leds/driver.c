#include <linux/module.h>
#include <linux/init.h>
#include <linux/fs.h>
#include <linux/moduleparam.h>
#include <linux/mm.h>
#include <linux/kernel.h>
//#include <linux/malloc.h>
#include <asm/uaccess.h>
#include <linux/errno.h>
#include <linux/slab.h>
#include <linux/cdev.h>
#include <linux/slab.h>
#include <asm/uaccess.h>
#include <asm/atomic.h>
#include <asm/unistd.h>
#include <linux/string.h>
#include <linux/list.h>
#include <linux/pci.h>
#include <mach/map.h>
#include <mach/regs-clock.h>
#include <mach/regs-gpio.h>

#include <plat/gpio-cfg.h>
#include <mach/gpio-bank-e.h>
#include <mach/gpio-bank-m.h>
#include <asm/irq.h>
#include <mach/hardware.h>

#define LIGHT_MAJOR 100

//#ifndef MULTINODES
//#define MULTINODES
//#define LIGHT_NUMBER 4
//#endif 	/*MULTINODES*/

#ifndef SIGLENODE
#define SIGLENODE
#define LIGHT_NUMBER 1
#endif 		/*SIGLENODE*/

#define OFF 0
#define ON 1
#define uchar unsigned char

int led_open(struct inode *inode, struct file *filp);
int led_release(struct inode *inode, struct file *filp);
ssize_t led_read(struct file *filp, char __user *buf, size_t count, loff_t *f_pos);
ssize_t led_write(struct file *filp, const char __user *buf, size_t count, loff_t *f_pos);
long led_ioctl(struct file *filp, unsigned int cmd, unsigned long arg);

struct file_operations led_fops = {
	.owner = THIS_MODULE,
	.read = led_read,
	.write = led_write,
	.open = led_open,
	.release = led_release,
	.unlocked_ioctl = led_ioctl,
};

struct led_dev{
	struct cdev cdev;	//led
	uchar state;		//SIGNLENODE:state低4位表示灯状态。
				//MULTINODES:state表示ON or OFF,必须创建多各节点，每个节点read函数才能获取对应的状态。
	struct semaphore sem;
};
struct led_dev *led_pdev;
int led_major = LIGHT_MAJOR;


static void led_cdev_init(struct led_dev *,  struct file_operations *, int);
static void led_cdev_del(struct led_dev *);


int led_open(struct inode *inode, struct file *filp){
	struct led_dev *dev;
	printk(KERN_ALERT "OPEN\n");	
	dev = container_of(inode->i_cdev, struct led_dev, cdev);
	filp->private_data = dev;	
	return 0;
}

int led_release(struct inode *inode, struct file *filp){
	printk(KERN_ALERT "CLOSE");
	return 0;
}

ssize_t led_read(struct file *filp, char __user *buf, size_t count, loff_t *f_pos){
	struct led_dev *dev;
	uchar buff[2];
	printk(KERN_ALERT "READ\n");
	dev = filp->private_data;
	buff[0] = dev->state;
	
	if (copy_to_user(buf, buff, 1)){
		return -EFAULT;
	}
	return 1;
}

ssize_t led_write(struct file *filp, const char __user *buf, size_t count, loff_t *f_pos){
	struct led_dev *dev;
	uchar buff[2];
	printk(KERN_ALERT "WRITE\n");
	dev = filp->private_data;
	if (copy_from_user(buff, buf, 1)){
		return -EFAULT;
	}
	
	//dev->state = buff[0];
	
	return 1;
}

long led_ioctl(struct file *filp, unsigned int cmd, unsigned long arg){
	struct led_dev *dev;
	printk(KERN_ALERT "CMD\n");
	dev = filp->private_data;	

	switch(cmd) {
		unsigned tmp;
	case 0:
	case 1:
                 if (arg > 3) 
                 {
		 	return -EINVAL;
		 }
		tmp = readl(S3C64XX_GPMDAT);
            
		if(cmd==1) //light ON
                  { 
			tmp &= (~(1<<arg));
			#ifdef SIGLENODE
			dev->state |= (1<<arg);
			#endif
			#ifdef MULTINODES
			dev->state = ON;
			#endif 
                  }
		else  //light OFF
                  { 
			tmp |= (1<<arg);
			#ifdef SIGLENODE	
			dev->state &= (~(1<<arg));
			#endif
			#ifdef MULTINODES
			dev->state = OFF;
			#endif 
                  }

                writel(tmp,S3C64XX_GPMDAT);

		//printk (DEVICE_NAME": %d %d\n", arg, cmd);
		return 0;
	default:
		return -EINVAL;
	}
}

static void led_cdev_init(struct led_dev * pdev, struct file_operations *fops, int index){
	int result;
  
	//init
	cdev_init(&pdev->cdev, fops);
	pdev->cdev.owner = THIS_MODULE;
	pdev->cdev.ops = fops;
	result = cdev_add(&pdev->cdev, MKDEV(led_major, index), 1);
//	printk(KERN_ALERT "init cdev%d...", index);
	if (result)
		printk(KERN_ALERT "Error %d adding LED%d", result, index);
}

static void led_cdev_del(struct led_dev * pdev){
	cdev_del(&pdev->cdev);	
}

static int led_init(void){
	int result;
	dev_t dev;
	int i = 0;
	unsigned tmp;

	printk(KERN_ALERT "load led driver...");
	
	dev = MKDEV(led_major, 0);
	if (led_major){
		result = register_chrdev_region(dev, LIGHT_NUMBER, "LED1");
	}else{
		result = alloc_chrdev_region(&dev, 0, LIGHT_NUMBER, "LED1");
		led_major = MAJOR(dev);
	}
	
	printk(KERN_ALERT "register_chrdev...");

	if (result < 0){
		return result;
	}
	//malloc led_pdev
	if (!(led_pdev = kmalloc(LIGHT_NUMBER*sizeof(struct led_dev), GFP_KERNEL))){
		result = -ENOMEM;
		goto fail_malloc;
	}
	printk(KERN_ALERT "malloc led_pdev...");

	memset(led_pdev, 0, LIGHT_NUMBER*sizeof(struct led_dev));
	
	//init cdev
	for (i=0;i<LIGHT_NUMBER;i++){
		led_cdev_init(&led_pdev[i], &led_fops, i);
		led_pdev[i].state = OFF;
		printk(KERN_ALERT "init cdev%d...", i);
	}

	//gpm0-3 pull up
	tmp = readl(S3C64XX_GPMPUD);
	tmp &= (~0xFF);
	tmp |= 0xaa;
	writel(tmp,S3C64XX_GPMPUD);

	//gpm0-3 output mode
	tmp =readl(S3C64XX_GPMCON);
	tmp &= (~0xFFFF);
	tmp |= 0x1111;
	writel(tmp,S3C64XX_GPMCON);
	
	//gpm0-3 output 0
	tmp = __raw_readl(S3C64XX_GPMDAT);
	tmp |= 0x0F;
	writel(tmp,S3C64XX_GPMDAT); 
	
	printk(KERN_ALERT "LED Driver init\n");
	return 0;

	fail_malloc:
		unregister_chrdev_region(dev, LIGHT_NUMBER);
		return result;
}

static void led_exit(void){
	int i = 0;
	for (i=0;i<LIGHT_NUMBER;i++){
		led_cdev_del(&led_pdev[i]);
	}

	printk(KERN_ALERT "del cdev...");
	
	kfree(led_pdev);
	printk(KERN_ALERT "free led_pdev...");

	unregister_chrdev_region(MKDEV(led_major, 0), LIGHT_NUMBER);
	printk(KERN_ALERT "unregister_chrdev...");
	
        printk(KERN_ALERT "del led driver...\n");
}

module_init(led_init);
module_exit(led_exit);

MODULE_AUTHOR("Zeqiang");
MODULE_LICENSE("Dual BSD/GPL");
MODULE_DESCRIPTION("A simple driver");
MODULE_ALIAS("a simplest module");


