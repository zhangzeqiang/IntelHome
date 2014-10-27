#include <linux/module.h>
#include <linux/vermagic.h>
#include <linux/compiler.h>

MODULE_INFO(vermagic, VERMAGIC_STRING);

struct module __this_module
__attribute__((section(".gnu.linkonce.this_module"))) = {
 .name = KBUILD_MODNAME,
 .init = init_module,
#ifdef CONFIG_MODULE_UNLOAD
 .exit = cleanup_module,
#endif
 .arch = MODULE_ARCH_INIT,
};

static const struct modversion_info ____versions[]
__used
__attribute__((section("__versions"))) = {
	{ 0xd0a8ddc4, "module_layout" },
	{ 0x98082893, "__copy_to_user" },
	{ 0x17a142df, "__copy_from_user" },
	{ 0xf6d8fc65, "kmalloc_caches" },
	{ 0xd6dbfaa9, "cdev_add" },
	{ 0x72df84f2, "cdev_init" },
	{ 0xfa2a45e, "__memzero" },
	{ 0x29537c9e, "alloc_chrdev_region" },
	{ 0x3ec0f0a8, "kmem_cache_alloc_trace" },
	{ 0xd8e484f0, "register_chrdev_region" },
	{ 0x7485e15e, "unregister_chrdev_region" },
	{ 0x37a0cba, "kfree" },
	{ 0x67e222cd, "cdev_del" },
	{ 0x2e5810c6, "__aeabi_unwind_cpp_pr1" },
	{ 0x27e1a049, "printk" },
};

static const char __module_depends[]
__used
__attribute__((section(".modinfo"))) =
"depends=";


MODULE_INFO(srcversion, "C89FD6F7976316E199B23DA");
