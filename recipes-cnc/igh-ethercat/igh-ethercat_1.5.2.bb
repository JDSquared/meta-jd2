SUMMARY = "IGH Ethercat Master for Linux"
HOMEPAGE = "http://www.etherlab.org/en/ethercat/"
SECTION = "lib/cnc"

LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=59530bdf33659b29e73d4adb9f9f6552"

SRC_URI = " http://www.etherlab.org/download/ethercat/ethercat-${PV}.tar.bz2"

SRC_URI[md5sum] = "6b4001f8d975865d74a0b108b3bdda3d"
SRC_URI[sha256sum] = "5f34ef3a5e1b8666ae77650917d0ec6eb4d7a437b3b66ea667a61158c8f4e8c4"

S = "${WORKDIR}/ethercat-${PV}"

inherit autotools pkgconfig

do_configure[depends] += "virtual/kernel:do_shared_workdir"
