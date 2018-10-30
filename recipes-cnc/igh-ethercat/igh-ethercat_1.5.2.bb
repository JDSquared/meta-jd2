SUMMARY = "IGH Ethercat Master for Linux"
HOMEPAGE = "http://www.etherlab.org/en/ethercat/"
SECTION = "lib/cnc"

LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=59530bdf33659b29e73d4adb9f9f6552"

S = "${WORKDIR}/git"

SRCBRANCH = "stable-1.5"
SRCREV = "f1942fdb564edec9a067c7e0c487f2d53b5f548b"
ETH_SRC ?= "git://github.com/JDSquared/etherlabmaster.git;protocol=https"
SRC_URI = "${ETH_SRC};branch=${SRCBRANCH}"

inherit autotools pkgconfig

EXTRA_OECONF = " --with-linux-dir=${STAGING_KERNEL_BUILDDIR} --prefix=/usr --sysconfdir=/etc --localstatedir=/var --disable-8139too --disable-e100 --disable-e1000 --disable-e1000e --disable-r8169"

do_configure[depends] += "virtual/kernel:do_compile_kernelmodules"
do_configure_prepend () {
    cd ${S}
    ./bootstrap
}

do_compile() {
    oe_runmake all modules
}
