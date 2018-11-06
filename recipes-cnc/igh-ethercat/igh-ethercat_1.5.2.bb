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
            file://0060_systemd_unit.patch \
            file://update-ethercat-config \
            file://99-ethercat.rules \
"

inherit autotools module systemd

do_configure[depends] += "virtual/kernel:do_compile_kernelmodules"
do_configure () {
    # Make a combined linux src directory for this package to compile.
    # It's slow and heavy, but this is the easiest way to get it to work for now.
    mkdir ${WORKDIR}/linux_combined || true
    rm -rf ${WORKDIR}/linux_combined/*

    cp -a ${STAGING_KERNEL_DIR}/. ${WORKDIR}/linux_combined/
    cp -a ${STAGING_KERNEL_BUILDDIR}/. ${WORKDIR}/linux_combined/

    cd ${S}
    ./bootstrap
    oe_runconf --with-linux-dir=${WORKDIR}/linux_combined --prefix=${prefix} \
     --sysconfdir=${sysconfdir} --localstatedir=${localstatedir} \
     --disable-8139too --disable-e100 --disable-e1000 --disable-e1000e \
     --disable-r8169 --enable-generic --enable-hrtimer --enable-sii-assign
}

do_compile() {    
    cd ${S}

    # Compile the ethercat tool program
    oe_runmake all

    # Now compile the modules. Recompile soe_errors since it now has
    # to be compiled like the kernel modules and we get architecture
    # merge errors if we don't touch this.
    touch ${S}/master/soe_errors.c

    #oe_runmake modules
}

do_install() {
    cd ${S}

    # Install the development headers
    install -d ${D}${includedir}
    install -m 0644 ${S}/include/ecrt.h ${D}${includedir}/
    install -m 0644 ${S}/include/ectty.h ${D}${includedir}/
    
    # Install the libraries
	oe_libinstall -so libethercat ${D}${libdir}

    # Install the user edited config file
    install -d ${D}${sysconfdir}/
    install -m 0644 ${S}/script/ethercat.conf ${D}${sysconfdir}

    # Install systemd files
    install -d ${D}${sbindir}
    install -m 0755 ${S}/script/ethercatctl ${D}${sbindir}/
    install -d ${D}${systemd_system_unitdir}
    install -m 0755 ${S}/script/ethercat.service ${D}${systemd_system_unitdir}/

    # Install the ethercat program
    install -d ${D}${bindir}
    install -m 0755 ${S}/tool/ethercat ${D}${bindir}/

    # Install extras
    install -d ${D}${sbindir}
    install -m 0755 ${WORKDIR}/update-ethercat-config ${D}${sbindir}/

    install -d ${D}${sysconfdir}/udev/rules.d
    install -m 0644 ${WORKDIR}/99-ethercat.rules ${D}${sysconfdir}/udev/rules.d/

    # Install modules
    #oe_runmake DEPMOD=echo MODLIB="${D}${nonarch_base_libdir}/modules/${KERNEL_VERSION}" modules_install
}

KERNEL_MODULES_META_PACKAGE = "${PN}"

FILES_${PN} += " \
                ${sysconfdir}/ethercat.conf \
                ${sbindir}/ethercatctl \
                ${bindir}/ethercat \
                ${systemd_system_unitdir}/ethercat.service \
                ${sbindir}/update-ethercat-config \
                ${sysconfdir}/udev/rules.d/99-ethercat.rules \
                ${libdir}/libethercat* \
"

FILES_${PN}-dev += " \
    ${includedir}/ecrt.h \
    ${includedir}/ectty.h \
"
