SUMMARY = "U-Boot for JD Squared i.MX boards"
require u-boot-common.inc
require recipes-bsp/u-boot/u-boot.inc

inherit fsl-u-boot-localversion

LOCALVERSION_imx7-jd2-ethcb-var-som = "-mx7"

PROVIDES += "u-boot"

PACKAGE_ARCH = "${MACHINE_ARCH}"
COMPATIBLE_MACHINE = "(mx7)"
