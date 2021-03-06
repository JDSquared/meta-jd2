# Freescale i.MX SOC extra configuration udev rules
FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI_append_mx7 = " file://blacklist-evbug.conf"

do_install_prepend () {
    if [ -e "${WORKDIR}/blacklist-evbug.conf" ]; then
        install -d ${D}${sysconfdir}/modprobe.d
        install -m 0644 ${WORKDIR}/blacklist-evbug.conf ${D}${sysconfdir}/modprobe.d
    fi
}

FILES_${PN}_append = " ${sysconfdir}/modprobe.d"

PACKAGE_ARCH_mx7 = "${MACHINE_SOCARCH}"
