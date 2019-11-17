import requests



def main():

    r = requests.get("https://www.facebook.com")
    print(r)


#print(r.status_code)

#x = r.headers
#print(x)


#print(r.headers['content-type'])
    #print(r)
