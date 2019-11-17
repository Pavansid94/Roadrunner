import requests
from bs4 import BeautifulSoup


r = requests.get(
    'https://en.wikipedia.org/wiki/Albert_Einstein')

if r.status_code == 200:
	soup: BeautifulSoup = BeautifulSoup(r.content, "html.parser")

for infobox in soup.find_all("table", {"class": "infobox biography vcard"}):
    print(infobox.text)