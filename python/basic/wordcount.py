#!/usr/bin/python3
# Copyright 2010 Google Inc.
# Licensed under the Apache License, Version 2.0
# http://www.apache.org/licenses/LICENSE-2.0

# Google's Python Class
# http://code.google.com/edu/languages/google-python-class/

"""Wordcount exercise
Google's Python class

The main() below is already defined and complete. It calls print_words()
and print_top() functions which you write.

1. For the --count flag, implement a print_words(filename) function that counts
how often each word appears in the text and prints:
word1 count1
word2 count2
...

Print the above list in order sorted by word (python will sort punctuation to
come before letters -- that's fine). Store all the words as lowercase,
so 'The' and 'the' count as the same word.

2. For the --topcount flag, implement a print_top(filename) which is similar
to print_words() but which prints just the top 20 most common words sorted
so the most common word is first, then the next most common, and so on.

Use str.split() (no arguments) to split on all whitespace.

Workflow: don't build the whole program at once. Get it to an intermediate
milestone and print your data structure and sys.exit(0).
When that's working, try for the next milestone.

Optional: define a helper function to avoid code duplication inside
print_words() and print_top().

"""

import sys

def get_map(filename):
  dictionary = {}
  
  f = open(filename, 'rt', encoding = 'utf-8')
  for line in f:
    lower_case_line = line.lower()
    words = lower_case_line.split()
    for word in words:
      count = 0
      if word in dictionary:
        count = dictionary[word]
      count += 1
      dictionary[word] = count
  f.close()
  
  return dictionary
      
      
def print_words(filename):
  print('print_words', filename)
  dictionary = get_map(filename)
  for word in sorted(dictionary.keys()):
    print(word, dictionary[word])

def print_top(filename):
  print('print_top', filename)
  dictionary = get_map(filename)
  sorted_by_count = sorted(dictionary.items(), key=lambda x:x[1], reverse=True)
  
  for item in sorted_by_count[:20]:
    print(item[0], item[1])
  #for index in indexes:
  #  print(sorted_by_count.)

def main():
  if len(sys.argv) != 3:
    print('usage: ./wordcount.py {--count | --topcount} file')
    sys.exit(1)

  option = sys.argv[1]
  filename = sys.argv[2]
  if option == '--count':
    print_words(filename)
  elif option == '--topcount':
    print_top(filename)
  else:
    print('unknown option: ' + option)
    sys.exit(1)

if __name__ == '__main__':
  main()
