SUMMARY = "IGH Ethercat Master for Linux"
HOMEPAGE = "http://www.etherlab.org/en/ethercat/"
SECTION = "lib/cnc"

LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=59530bdf33659b29e73d4adb9f9f6552"

S = "${WORKDIR}/git"

SRCBRANCH = "stable-1.5"
SRCREV = "f1942fdb564edec9a067c7e0c487f2d53b5f548b"
ETH_SRC ?= "git://github.com/JDSquared/etherlabmaster.git;protocol=https"
SRC_URI = "${ETH_SRC};branch=${SRCBRANCH} \
            file://0010_fix_distclean.patch \
            file://0020_fix_read_pdo_count.patch \
            file://0030_relax_al_state_change_timeout.patch \
            file://0040_RTAI_module_cflags.patch \
            file://0050_install_headers.patch \
            file://0060_systemd_unit.patch \
"

inherit autotools pkgconfig

EXTRA_OECONF = " --with-linux-dir=${WORKDIR}/linux_combined --prefix=/usr --sysconfdir=/etc --localstatedir=/var --disable-8139too --disable-e100 --disable-e1000 --disable-e1000e --disable-r8169 --enable-generic"

do_configure[depends] += "virtual/kernel:do_compile_kernelmodules"
do_configure () {
    # Make a combined linux src directory for this package to compile.
    # It's slow and heavy, but this is the easiest way to get it to work for now.
    mkdir ${WORKDIR}/linux_combined || true
    rm -rf ${WORKDIR}/linux_combined/*

    cp -r ${STAGING_KERNEL_DIR}/* ${WORKDIR}/linux_combined/
    cp -r ${STAGING_BUILD_DIR}/* ${WORKDIR}/linux_combined/

    cd ${S}
    ./bootstrap
    oe_runconf
}

do_compile() {
    cd ${S}
    oe_runmake all modules
}

do_install() {

}
