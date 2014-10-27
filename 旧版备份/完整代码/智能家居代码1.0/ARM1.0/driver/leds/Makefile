#KVERS = $(shell uname -r)
KVERS = 3.0.1
ifneq ($(KERNELRELEASE),)
	obj-m += driver.o
else
	KERNELDIR ?= /lib/modules/$(KVERS)/build
	PWD := $(shell pwd)
default:
	$(MAKE) -C $(KERNELDIR) M=$(PWD) modules
endif
