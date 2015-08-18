#!/usr/bin/env python

import logger
logger.config(10)
_logger = logger.getLogger("lvlimit")
_logger.info("Initializing")

import struct
import urllib2
import sys
import cherrypy

target = sys.argv[1]
listen_host = sys.argv[2]
listen_port = int(sys.argv[3])

class LiveviewLimiter(object):
	@cherrypy.expose
	def lv(self, div):
		div = int(div)
		h = urllib2.urlopen(target)

		frame = 0
		while True:
			frame = frame + 1

			(data, timestamp) = read_one_frame(handle)
			size_str = struct.pack('!I', len(data))

			if frame % div != 0:
				continue

			yield (size_str + data)
	lv._cp_config = {'response.stream': True}

def read_one_frame(handle):
	common_header = handle.read(8)
	payload_header = handle.read(128)

	timestamp = (ord(common_header[4]) << 24) | (ord(common_header[5]) << 16) | (ord(common_header[6]) << 8) | (ord(common_header[7]) << 0)
	data_size = (ord(payload_header[4]) << 16) | (ord(payload_header[5]) << 8) | ord(payload_header[6])
	padding_size = ord(payload_header[7])

	data = handle.read(data_size)
	padding = handle.read(padding_size)

	return (data, timestamp)

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

