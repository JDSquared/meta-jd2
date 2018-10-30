SUMMARY = "IGH Ethercat Master for Linux"
HOMEPAGE = "http://www.etherlab.org/en/ethercat/"
SECTION = "lib/cnc"

LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=59530bdf33659b29e73d4adb9f9f6552"

S = "${WORKDIR}/git"

SRCBRANCH = "stable-1.5"
SRCREV = "f1942fdb564edec9a067c7e0c487f2d53b5f548b"
KERNEL_SRC ?= "git://github.com/JDSquared/etherlabmaster.git;protocol=https"
SRC_URI = "${KERNEL_SRC};branch=${SRCBRANCH}"

inherit autotools pkgconfig

EXTRA_OECONF = " --prefix=/usr --sysconfdir=/etc --localstatedir=/var --disable-8139too --disable-e100 --disable-e1000 --disable-e1000e --disable-r8169"

do_configure[depends] += "virtual/kernel:do_shared_workdir"
do_configure_prepend () {
    cd ${S}
    ./bootstrap
}