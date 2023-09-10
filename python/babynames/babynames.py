#!/usr/bin/python3
# Copyright 2010 Google Inc.
# Licensed under the Apache License, Version 2.0
# http://www.apache.org/licenses/LICENSE-2.0

# Google's Python Class
# http://code.google.com/edu/languages/google-python-class/

import sys
import re

"""Baby Names exercise

Define the extract_names() function below and change main()
to call it.

For writing regex, it's nice to include a copy of the target
text for inspiration.

Here's what the html looks like in the baby.html files:
...
<h3 align="center">Popularity in 1990</h3>
....
<tr align="right"><td>1</td><td>Michael</td><td>Jessica</td>
<tr align="right"><td>2</td><td>Christopher</td><td>Ashley</td>
<tr align="right"><td>3</td><td>Matthew</td><td>Brittany</td>
...

Suggested milestones for incremental development:
 -Extract the year and print it
 -Extract the names and rank numbers and just print them
 -Get the names data into a dict and print it
 -Build the [year, 'name rank', ... ] list and print it
 -Fix main() to use the extract_names list
"""

def extract_names(filename):
  """
  Given a file name for baby.html, returns a list starting with the year string
  followed by the name-rank strings in alphabetical order.
  ['2006', 'Aaliyah 91', Aaron 57', 'Abagail 895', ' ...]
  """
  print('extract_names', filename)
  
  text = ''
  with open(filename, 'rt', encoding='utf-8') as f:
    text = f.read()
  f.close()
  
  year_found = None
  year_match = re.search(r'Popularity\ in\ (\d\d\d\d)', text)
  if year_match:
    year_found = year_match.group(1)

  #Extract names and rank numbers
  #<tr align="right"><td>1</td><td>Michael</td><td>Jessica</td>
  tuples = re.findall(r'<td>(\d+)</td><td>(\w+)</td><td>(\w+)</td>', text)
  names_to_rank = {}
  for tuple in tuples:
    rank = tuple[0]
    boy_name = tuple[1]
    girl_name = tuple[2]
    if boy_name not in names_to_rank:
      names_to_rank[boy_name] = rank
    if girl_name not in names_to_rank:
      names_to_rank[girl_name] = rank

  names = []
  sorted_names = sorted(names_to_rank.keys())
  for name in sorted_names:
    to_append = name + ' ' + names_to_rank[name]
    names.append(to_append)

  print(names)

  return names


def main():
  # This command-line parsing code is provided.
  # Make a list of command line arguments, omitting the [0] element
  # which is the script itself.
  args = sys.argv[1:]

  if not args:
    print('usage: [--summaryfile] file [file ...]')
    sys.exit(1)

  # Notice the summary flag and remove it from args if it is present.
  summary = False
  if args[0] == '--summaryfile':
    summary = True
    extract_names(args[2])
    del args[0]

  # +++your code here+++
  # For each filename, get the names, then either print the text output
  # or write it to a summary file

if __name__ == '__main__':
  main()
