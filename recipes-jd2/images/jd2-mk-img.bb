# Copyright (C) 2018 JD Squared, Inc.
# Released under the MIT license (see COPYING.MIT for the terms)

DESCRIPTION = "JD Squared Machinekit + Ethercat image"
LICENSE = "MIT"
DEFAULT_USER = "mchadmn"
DEFAULT_PASS = "bmoc"

inherit core-image distro_features_check extrausers

IMAGE_FEATURES += " \
    ssh-server-openssh \
"

CORE_IMAGE_EXTRA_INSTALL += " \
	packagegroup-core-full-cmdline \
	packagegroup-core-buildessential \
	modutils \
	nano \
	nodejs \
	flex \
	git \
	m4 \
	iperf3 \
	libtool \
	python-compiler \
	strace \
	openssl \
	igh-ethercat \
"

EXTRA_USERS_PARAMS = " \
	useradd ${DEFAULT_USER}; \
	usermod -P ${DEFAULT_PASS} ${DEFAULT_USER}; \
	usermod -a -G sudo ${DEFAULT_USER}; \
	usermod -a -G ethercat ${DEFAULT_USER}; \
"

# Enable sudo group, and give default user passwordless sudo
update_sudoers(){
    sed -i 's/# %sudo/%sudo/' ${IMAGE_ROOTFS}/etc/sudoers
	echo "${DEFAULT_USER}  ALL=NOPASSWD: ALL" >> ${IMAGE_ROOTFS}/etc/sudoers
}

ROOTFS_POSTPROCESS_COMMAND += " update_sudoers;"

fixup_sshd(){
	# Enable PAM for ssh links
	# Fixes an issue where users cannot change ulimits when logged in via
	# ssh, which causes some Machinekit functions to fail
	sed -i 's/^#UsePAM.*$/UsePam yes/' ${IMAGE_ROOTFS}/etc/ssh/sshd_config
	# Disable GSSAPI authentication to speed up ssh authentication
	sed -i 's/^#GSSAPIAuthentication.*$/GSSAPIAuthentication no/' ${IMAGE_ROOTFS}/etc/ssh/sshd_config
	# Don't allow root login over ssh
	sed -i 's/^#PermitRootLogin.*$/PermitRootLogin no/' ${IMAGE_ROOTFS}/etc/ssh/sshd_config
}

ROOTFS_POSTPROCESS_COMMAND += " fixup_sshd;"
