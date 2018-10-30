SUMMARY = "IGH Ethercat Master for Linux"
HOMEPAGE = "http://www.etherlab.org/en/ethercat/"
SECTION = "lib/cnc"

LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=59530bdf33659b29e73d4adb9f9f6552"

SRCREV = "c644d50c2bea"
ETH_SRC ?= "hg://hg.code.sf.net/p/etherlabmaster/code;module=ethercat-hg;"
SRC_URI = "${ETH_SRC};rev=${SRCREV}"

S = "${WORKDIR}/ethercat-${PV}"

inherit autotools pkgconfig

do_configure[depends] += "virtual/kernel:do_shared_workdir"
