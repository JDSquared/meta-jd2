SUMMARY = "IGH Ethercat Master for Linux"
HOMEPAGE = "http://www.etherlab.org/en/ethercat/"
SECTION = "lib/cnc"

LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=59530bdf33659b29e73d4adb9f9f6552"

S = "${WORKDIR}/git"
TMPINST = "${WORKDIR}/tmpinstall"

SRCBRANCH = "stable-1.5"
SRCREV = "f1942fdb564edec9a067c7e0c487f2d53b5f548b"
ETH_SRC ?= "git://github.com/JDSquared/etherlabmaster.git;protocol=https"
SRC_URI = "${ETH_SRC};branch=${SRCBRANCH} \
            file://0010_fix_distclean.patch \
            file://0020_fix_read_pdo_count.patch \
            file://0030_relax_al_state_change_timeout.patch \
            file://0040_RTAI_module_cflags.patch \
            file://0060_systemd_unit.patch \
"

inherit autotools module systemd

do_configure[depends] += "virtual/kernel:do_compile_kernelmodules"
do_configure () {
    unset CFLAGS CPPFLAGS CXXFLAGS LDFLAGS
    # Make a combined linux src directory for this package to compile.
    # It's slow and heavy, but this is the easiest way to get it to work for now.
    mkdir ${WORKDIR}/linux_combined || true
    rm -rf ${WORKDIR}/linux_combined/*

    cp -a ${STAGING_KERNEL_DIR}/. ${WORKDIR}/linux_combined/
    cp -a ${STAGING_KERNEL_BUILDDIR}/. ${WORKDIR}/linux_combined/

    cd ${S}
    ./bootstrap
    oe_runconf --with-linux-dir=${WORKDIR}/linux_combined --prefix=/usr --sysconfdir=/etc --localstatedir=/var --disable-8139too --disable-e100 --disable-e1000 --disable-e1000e --disable-r8169 --enable-generic
}

do_compile() {
    unset CFLAGS CPPFLAGS CXXFLAGS LDFLAGS
    cd ${S}
    oe_runmake CC="${KERNEL_CC}" LD="${KERNEL_LD}" \
		   AR="${KERNEL_AR}" \
		   KBUILD_EXTRA_SYMBOLS="${KBUILD_EXTRA_SYMBOLS}" \
           all modules
}

do_install() {
    unset CFLAGS CPPFLAGS CXXFLAGS LDFLAGS
    cd ${S}
    mkdir -p ${TMPINST} || true

    # Install the modules in the split kernel directory
    oe_runmake DEPMOD=echo MODLIB="${D}${nonarch_base_libdir}/modules/{KERNEL_VERSION}" \
            O="${STAGING_KERNEL_BUILDDIR}" \
            DESTDIR=${TMPINST} \
            modules_install install
    
	if [ ! -e "${B}/${MODULES_MODULE_SYMVERS_LOCATION}/Module.symvers" ] ; then
		bbwarn "Module.symvers not found in ${B}/${MODULES_MODULE_SYMVERS_LOCATION}"
		bbwarn "Please consider setting MODULES_MODULE_SYMVERS_LOCATION to a"
		bbwarn "directory below B to get correct inter-module dependencies"
	else
		install -Dm0644 "${B}/${MODULES_MODULE_SYMVERS_LOCATION}"/Module.symvers ${D}${includedir}/${BPN}/Module.symvers
		# Module.symvers contains absolute path to the build directory.
		# While it doesn't actually seem to matter which path is specified,
		# clear them out to avoid confusion
		sed -e 's:${B}/::g' -i ${D}${includedir}/${BPN}/Module.symvers
	fi
}

KERNEL_MODULES_META_PACKAGE = "${PN}"

FILES_${PN} += " ethercat.conf \  
"
