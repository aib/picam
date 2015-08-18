#!/usr/bin/env python

import logger
logger.config(10)
_logger = logger.getLogger("lvlimit")
_logger.info("Initializing")

import struct
import urllib2
import sys
import cherrypy
import cv2

base_url = sys.argv[1]
listen_host = sys.argv[2]
listen_port = int(sys.argv[3])

class LiveviewLimiter(object):
	@cherrypy.expose
	def vc(self, div):
		div = int(div)

		try:
			target = 0
			_logger.debug("Opening video capture stream %i", target)
			cam = cv2.VideoCapture(target)

			frame = 0
			while True:
				frame = frame + 1

				(ret, frame_data) = cam.read()
				data = '' #TODO
				size_str = struct.pack('!II', 0, len(data))

				if frame % div != 0:
					continue;

				yield (size_str + data)
		finally:
			_logger.debug("Closing video capture stream %i", target)
			cam.release()
	vc._cp_config = {'response.stream': True}

	@cherrypy.expose
	def lv(self, div):
		div = int(div)

		try:
			target = base_url + "/liveview/liveviewstream"
			_logger.debug("Opening stream to %s", target)
			handle = urllib2.urlopen(target)
			frame = 0
			while True:
				frame = frame + 1

				data = read_one_sony_frame(handle)
				size_str = struct.pack('!II', timestamp, len(data))

				if frame % div != 0:
					continue

				yield (size_str + data)
		finally:
			_logger.debug("Closing stream to %s", target)
			handle.close()
	lv._cp_config = {'response.stream': True}

def read_one_sony_frame(handle):
	common_header = handle.read(8)
	payload_header = handle.read(128)

	data_size = (ord(payload_header[4]) << 16) | (ord(payload_header[5]) << 8) | ord(payload_header[6])
	padding_size = ord(payload_header[7])

	data = handle.read(data_size)
	padding = handle.read(padding_size)

	return data

def main():
	cherrypy.config.update(
		{ 'server.socket_host': listen_host, 'server.socket_port': listen_port, 'log.screen': False }
	)
	cherrypy.quickstart(
		LiveviewLimiter(),
		'/',
		{ '/': {} }
	)

if __name__ == '__main__':
	main()

