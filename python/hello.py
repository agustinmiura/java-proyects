#!/usr/bin/python3
# Copyright 2010 Google Inc.
# Licensed under the Apache License, Version 2.0
# http://www.apache.org/licenses/LICENSE-2.0

# Google's Python Class
# http://code.google.com/edu/languages/google-python-class/

"""A tiny Python program to check that Python is working.
Try running this program from the command line like this:
  python hello.py
  python hello.py Alice
That should print:
  Hello World -or- Hello Alice
Try changing the 'Hello' to 'Howdy' and run again.
Once you have that working, you're ready for class -- you can edit
and run Python code; now you just need to learn Python!
"""

import sys

def list():
  
  colors = ['red', 'blue', 'green']
  
  print(colors[0])
  
  print(colors[1])
  
  print(len(colors))
  
  print('called list method')
  
  b = colors
  
  squares = [1,4,9,16]
  sum = 0
  for num in squares: 
    sum += num
  print(sum)
  
  list = ['larry', 'currly', 'moe']
  if 'moe' in list:
    print('Yay')
    
  for i in range(100):
    print(i)
    
  a = [1,2,3,4]
  i = 0
  while i < len(a):
    print(a[i])
    i+=3
    
  list = ['larry', 'currly', 'moe']
  
  list.append('shemp')
  print(list)
  
  list.insert(0, 'xxxx')
  print(list)
  
  list.extend(['zzz','zzzz2'])
  print(list)
  
  print(list.index('moe'))
  
  list.pop()
  print(list)
  
  list.pop(1)
  print(list)
  
  list = ['a', 'b', 'c', 'd']
  print(list[1:-1])
  
  list[0:2] = 'z'
  print(list)
    

def main():
  list()
  # Get the name from the command line, using 'World' as a fallback.
  if len(sys.argv) >= 2:
    name = sys.argv[1]
  else:
    name = 'World'
  print('Hello', name)

# This is the standard boilerplate that calls the main() function.
if __name__ == '__main__':
  main()
