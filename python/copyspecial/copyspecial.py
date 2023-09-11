#!/usr/bin/python3
# Copyright 2010 Google Inc.
# Licensed under the Apache License, Version 2.0
# http://www.apache.org/licenses/LICENSE-2.0

# Google's Python Class
# http://code.google.com/edu/languages/google-python-class/

import sys
import re
import os
import shutil
import subprocess
import traceback

"""Copy Special exercise
"""

def zip_to(paths, zippath):
  print('Called zip_to:', paths,'zippath:',zippath)
  try:
    cmd = 'zip -r ' + zippath + ' ' + paths
    (status, output) = subprocess.getstatusoutput(cmd)
    if status:
      sys.stderr.write(output)
      sys.exit(status)
    print(output)
  except Exception:
    print('Error with the zip command')
    print(traceback.format_exc())

def copy_to(paths, dir):
  print('Called copy_to with paths:',paths,'dir:',dir)
  try:
    filenames = os.listdir(paths)
    for filename in filenames:
      shutil.copy(filename, dir)
  except Exception:
    print('Error copying the files')
    print(traceback.format_exc())
        
def get_special_paths(dir):
  print('Called get_special_paths with ',dir)
  filenames = os.listdir(dir)
  return filenames

def main():
  # This basic command line argument parsing code is provided.
  # Add code to call your functions below.

  # Make a list of command line arguments, omitting the [0] element
  # which is the script itself.
  args = sys.argv[1:]
  if not args:
    print('usage: [--todir dir][--tozip zipfile] dir [dir ...]')
    sys.exit(1)

  # todir and tozip are either set from command line
  # or left as the empty string.
  # The args array is left just containing the dirs.
  todir = ''
  if args[0] == '--todir':
    todir = args[1]
    origin = args[1]
    destination = args[2]
    copy_to(origin, destination)
    del args[0:2]

  tozip = ''
  if args[0] == '--tozip':
    tozip = args[1]
    paths = args[2]
    zip_to(paths, tozip)
    del args[0:2]

  if not args: # A zero length array evaluates to "False".
    print('error: must specify one or more dirs')
    sys.exit(1)

  # +++your code here+++
  # Call your functions
  if todir == '' and tozip == '':
    dir = args[0]
    files = get_special_paths(dir)
    for file in files:
      print(os.path.abspath(os.path.join(dir, file)))


  
if __name__ == '__main__':
  main()
