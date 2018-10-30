
DESCRIPTION = "Startup service to disable runtime power management on imx and avoid hang"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=59530bdf33659b29e73d4adb9f9f6552"

SRC_URI = "file://disable-runtime-power-mgmt.sh \
           file://disable-runtime-power-mgmt.service"

S = "${WORKDIR}"

inherit systemd

SYSTEMD_SERVICE_${PN} = "disable-runtime-power-mgmt.service"

FILES_${PN} += " disable-runtime-power-mgmt.service \
                 disable-runtime-power-mgmt.sh"

do_install () {
    install -d ${D}${libexec}/
    install -m 0755 ${S}/disable-runtime-power-mgmt.sh ${D}${libexec}/disable-runtime-power-mgmt.sh
    install -d ${D}${systemd_system_unitdir}
    install -m 0755 ${S}/disable-runtime-power-mgmt.service ${D}${systemd_system_unitdir}/
}
