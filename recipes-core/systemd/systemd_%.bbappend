FILESEXTRAPATHS_prepend := "${THISDIR}/systemd:"

SRC_URI += " \
    file://userint.network \
"

PACKAGECONFIG_append = " networkd"

do_install_append() {
    # The network files need to be in /usr/lib/systemd, not ${systemd_unitdir}...
    install -d ${D}${nonarch_base_libdir}/systemd/network/
    install -m 0644 ${WORKDIR}/userint.network ${D}${nonarch_base_libdir}/systemd/network/
}

FILES_${PN} += " \
    ${nonarch_base_libdir}/systemd/network \
"
