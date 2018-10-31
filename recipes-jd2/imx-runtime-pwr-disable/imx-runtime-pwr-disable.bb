
DESCRIPTION = "Startup service to disable runtime power management on imx and avoid hang"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

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
