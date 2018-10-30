# Copyright (C) 2018 JD Squared, Inc.

SUMMARY = "JD Squared CNC image requirements"
LICENSE = "MIT"

PACKAGE_ARCH = "${MACHINE_ARCH}"

inherit packagegroup

PACKAGES = " \
    ${PN} \
"

# The essential packages for device bootup that may be set in the
# machine configuration file.
MACHINE_ESSENTIAL_EXTRA_RDEPENDS ?= ""

RDEPENDS_${PN} = " \
    igh-ethercat \
    ${MACHINE_ESSENTIAL_EXTRA_RDEPENDS} \
"

COMPATIBLE_MACHINE = "(imx7d-jd2-ethcb-var-som)"
