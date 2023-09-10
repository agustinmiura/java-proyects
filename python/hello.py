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
    

def sort_practice():

  a = [4,1,12,6,5]
  print(sorted(a))
  
  print(a)
  
  strings = ['aa', 'BB', 'zz', 'CC']
  print(sorted(strings))
  print(sorted(strings, reverse = True))
  
  strings = ['a','aa', 'aaa']
  print(sorted(strings, key=len))
  
  strings = ['a', 'AA', 'b']
  print(sorted(strings, key=str.lower))
  
  strings = ['xc', 'zb', 'yd', 'wa']
  def myFn(s):
    return s[-1]
  
  print(sorted(strings, key=myFn))
  
  from operator import itemgetter
  
  grade = [('Freddy', 'Frank', 3), ('Anil', 'Frank', 100), ('Anil', 'Wang', 24)]
  print(sorted(grade, key=itemgetter(1,0)))
  
  print(sorted(grade, key=itemgetter(0,-1)))
    
  tuple = ('hi',)
  print(tuple)
  
  nums = [1,2,3,4]
  
  squares = [ n * n for n in nums]
  print(squares)
  
  strings = ['hello', 'and', 'goodbye']
  shouting = [ s.upper() + '!!!' for s in strings]
  print(shouting)
  
  nums = [2,8,1,6]
  small = [n for n in nums if n <= 2]
  print(small)
  
  fruits = ['apple', 'cherry', 'banana', 'lemon']
  aFruits = [ s.upper() for s in fruits if 'a' in s]
  print(aFruits)
  
def dictionary_practice():
  dictionary = {}
  dictionary['a'] = 'alpha'
  dictionary['g'] = 'gamma'
  dictionary['o'] = 'omega'
  
  print(dictionary)

  print(dictionary['a'])
  
  #print(dictionary['z'])
  
  if 'z' in dictionary: 
    print('Key')
    
  for key in dictionary.keys():
    print(key)
    
  for value in dictionary.values():
    print(value)
    
  print(dictionary.items())
  
  for k,v in dictionary.items():
    print(k,'>',v)
  
  h = {}

  var = 6
  del var 
  
  list = ['a', 'b', 'c', 'd']
  del list[0]
  print(list)
  
  del list[-2]
  print(list)
  
  dictionary = {'a':1, 'b':2, 'c':3}
  del dictionary['b']
  print(dictionary)
  
  f = open('foo.txt', 'rt', encoding = 'utf-8')
  for line in f:
    print(line, end='')
  f.close()
  
  with open('foo.txt', 'rt', encoding='utf-8') as f:
    for line in f:
      print(line)
  
  with open('write_test', encoding = 'utf-8', mode='wt') as f:
    f.write('\u20ACunicode\u20AC\n')
  
def main():
  dictionary_practice()
  #sort_practice()
  #list()
  # Get the name from the command line, using 'World' as a fallback.
  if len(sys.argv) >= 2:
    name = sys.argv[1]
  else:
    name = 'World'
  print('Hello', name)

# This is the standard boilerplate that calls the main() function.
if __name__ == '__main__':
  main()
