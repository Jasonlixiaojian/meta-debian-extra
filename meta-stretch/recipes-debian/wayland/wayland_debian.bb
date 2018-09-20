FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"
SRC_URI_append = "\
	file://Makefile.patch \
"
SUMMARY = "wayland compositor infrastructure"
DESCRIPTION = "\
Wayland is a protocol for a compositor to talk to its clients as well \
as a C library implementation of that protocol. The compositor can be \
a standalone display server running on Linux kernel modesetting and   \
evdev input devices, an X application, or a wayland client \
itself. The clients can be traditional applications, X servers \
(rootless or fullscreen) or other display servers. \
"
HOMEPAGE = "http://wayland.freedesktop.org/"
PR = "r0"
# Override value of DEBIAN_GIT_BRANCH variable in debian-package class
DEBIAN_GIT_BRANCH = "stretch-master"

inherit debian-package
PV = "1.12.0"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "\
	file://COPYING;md5=1d4476a7d98dd5691c53d4d43a510c72"
inherit autotools pkgconfig

DEPENDS_class-target += "lib${DPN}-dev-native lib${DPN}-bin-native libffi expat libxml2"
DEPENDS_class-native += "libffi-native expat-native libxml2-native"

#building documentation depend on doxygen which is not yet in meta-debian
EXTRA_OECONF += "--disable-documentation"
EXTRA_OEMAKE_class-target += "wayland_scanner=${STAGING_BINDIR_NATIVE}/wayland-scanner"
DEBIAN_PATCH_TYPE = "nopatch"

# Avoid a parallel build problem
PARALLEL_MAKE = ""
do_install_append() {
	#remove the unwanted files
	rm ${D}${libdir}/*.la
	rm ${D}${libdir}/*.a
}
PACKAGES = "lib${DPN}-dev lib${DPN}-bin lib${DPN}-client lib${DPN}-cursor lib${DPN}-server lib${DPN}-dbg"
PROVIDES = "lib${DPN}-dev lib${DPN}-bin lib${DPN}-client lib${DPN}-cursor lib${DPN}-server lib${DPN}-dbg"

FILES_lib${DPN}-cursor = "${libdir}/libwayland-cursor.so.*"
FILES_lib${DPN}-server = "${libdir}/libwayland-server.so.*"
FILES_lib${DPN}-client = "${libdir}/libwayland-client.so.*"
FILES_lib${DPN}-bin = "${bindir}/wayland-scanner \
                       ${datadir}/aclocal/wayland-scanner.m4 \
                       ${datadir}/wayland/wayland-scanner.mk \
                      "
FILES_lib${DPN}-dbg = "${libdir}/.debug/* \
                       ${bindir}/.debug/wayland-scanner \
                      "
FILES_lib${DPN}-dev = "${datadir}/wayland \
                    ${includedir}/*.h \
                    ${libdir}/*.a \
                    ${libdir}/*.so \
                    ${libdir}/pkgconfig/*.pc \
                    ${datadir}/wayland/wayland.* \
                   "

DEBIANNAME_lib${DPN}-cursor = "lib${DPN}-cursor0"
DEBIANNAME_lib${DPN}-server = "lib${DPN}-server0"
DEBIANNAME_lib${DPN}-client = "lib${DPN}-client0"
DEBIANNAME_lib${DPN}-dev    = "lib${DPN}-dev"

RDEPENDS_lib${DPN}-server += "libffi"
RDEPENDS_lib${DPN}-cursor += "lib${DPN}-client"
RDEPENDS_lib${DPN}-client += "libffi"

BBCLASSEXTEND = "native"
