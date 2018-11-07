FILESEXTRAPATHS_prepend := "${THISDIR}/systemd:"

SRC_URI += " \
    file://usrint.network \
    file://ethercat.network \
"

PACKAGECONFIG_append = " networkd"

do_install_append() {
    # The network files need to be systemd config directory...
    install -d ${D}${nonarch_base_libdir}/systemd/network/
    install -m 0644 ${WORKDIR}/usrint.network ${D}${nonarch_base_libdir}/systemd/network/
    install -m 0644 ${WORKDIR}/ethercat.network ${D}${nonarch_base_libdir}/systemd/network/
}

FILES_${PN} += " \
    ${nonarch_base_libdir}/systemd/network \
"
