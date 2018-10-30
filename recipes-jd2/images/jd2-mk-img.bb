# Copyright (C) 2018 JD Squared, Inc.
# Released under the MIT license (see COPYING.MIT for the terms)

DESCRIPTION = "JD Squared Machinekit + Ethercat image"
LICENSE = "MIT"

inherit core-image distro_features_check extrausers

IMAGE_FEATURES += " \
    ssh-server-openssh \
"

CORE_IMAGE_EXTRA_INSTALL += " \
	packagegroup-core-full-cmdline \
	packagegroup-core-buildessential \
	packagegroup-jd2-cnc \
	nodejs \
	flex \
	git \
	m4 \
	iperf3 \
	libtool \
	python-compiler \
	strace \
	openssl \
"

EXTRA_USERS_PARAMS = "useradd mchadmn; \
					  usermod -P bmoc mchadmn; \
					  usermod -a -G sudo mchadmn;"
