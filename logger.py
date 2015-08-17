import logging
import sys

level_colors = {
	logging.CRITICAL: ("1;35", "CRIT"),
	logging.ERROR:    ("1;31", "ERROR"),
	logging.WARNING:  ("1;33", "WARN"),
	logging.INFO:     ("1;39", "INFO"),
	logging.DEBUG:    ("0;39", "DEBUG"),
	logging.NOTSET:   ("1;30", "")
}

project_log_level = logging.NOTSET
_project_loggers = {}

def config(level):
	root_logger = logging.getLogger()
	root_logger.setLevel(level)

	console = logging.StreamHandler(sys.stdout)
	console.setFormatter(ColorFormatter())
	root_logger.addHandler(console)

	stderr = logging.StreamHandler(sys.stderr)
	stderr.setFormatter(ColorFormatter())
	stderr.setLevel(logging.ERROR)
	root_logger.addHandler(stderr)

	set_project_log_level(level)

def getLogger(name):
	logger = logging.getLogger(name)
	_project_loggers[name] = logger
	logger.setLevel(project_log_level)
	return logger

def set_project_log_level(level):
	global project_log_level
	project_log_level = level
	for name in _project_loggers:
		_project_loggers[name].setLevel(level)

class ColorFormatter(logging.Formatter):
	def __init__(self, fmt=None, datefmt=None):
		super(ColorFormatter, self).__init__(fmt, datefmt)

	def format(self, record):
		level = max(filter(lambda lvl: record.levelno >= lvl, level_colors))
		color_pre = "\033[" + level_colors[level][0] + "m"
		color_post = "\033[0m"
		level_label = level_colors[level][1]
		super(ColorFormatter, self).format(record)
		return "%s%s [%-5s] [%s:%s:%s] %s%s" % (color_pre,
		                                        super(ColorFormatter, self).formatTime(record),
		                                        level_label,
		                                        record.filename, record.lineno, record.funcName,
		                                        record.message,
		                                        color_post)
