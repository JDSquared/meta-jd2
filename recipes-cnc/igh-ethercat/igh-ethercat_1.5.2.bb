SUMMARY = "IGH Ethercat Master for Linux"
HOMEPAGE = "http://www.etherlab.org/en/ethercat/"
SECTION = "lib/cnc"

LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=59530bdf33659b29e73d4adb9f9f6552"

S = "${WORKDIR}/git"

SRCBRANCH = "master"
SRCREV = "c8d01403b41134384239f021ebabb0daa09dc896"
ETH_SRC ?= "git://github.com/ribalda/ethercat.git;protocol=https"
SRC_URI = "${ETH_SRC};branch=${SRCBRANCH} \
            file://0010_fix_distclean.patch \
            file://0060_systemd_unit.patch \
            file://update-ethercat-config \
            file://99-ethercat.rules \
"

inherit autotools systemd useradd module-base

USERADD_PACKAGES = "${PN}"
GROUPADD_PARAM_${PN} = "ethercat"

EXTRA_OECONF = "--with-linux-dir=${STAGING_KERNEL_BUILDDIR} --enable-eoe \
     --disable-8139too --disable-e100 --disable-e1000 --disable-e1000e \
     --disable-r8169 --enable-generic --enable-hrtimer --enable-sii-assign \
"

do_configure[depends] += "virtual/kernel:do_compile_kernelmodules"
do_configure () {  
    cd ${S}
    ./bootstrap
    oe_runconf
}

do_compile() {
    cd ${S}

    # Compile the ethercat tool program
    oe_runmake all
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
}

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
