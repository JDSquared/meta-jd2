SECTION = "bootloaders"

LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://Licenses/gpl-2.0.txt;md5=b234ee4d69f5fce4486a80fdaf4a4263"

SRCBRANCH = "imx_v2017.03_4.9.11_1.0.0_ga_jd201"
UBOOT_SRC = "git://github.com/JDSquared/u-boot-imx.git;protocol=https"
SRC_URI = "${UBOOT_SRC};branch=${SRCBRANCH}"
SRCREV = "2636f84d7c992c2e9a0647b650bbe288c0d2cf6c"

S = "${WORKDIR}/git"
