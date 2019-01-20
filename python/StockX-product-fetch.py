# StockX API Retriever for general product info population
######################
import requests
import firebase_admin
from firebase_admin import credentials, firestore

# Test product retrieval IDs
YEEZY_0_UUID = "1a277efe-4aa1-41d2-b33a-4a08b256b046"
JORDAN_0_UUID = "5ee9da0d-d10a-4d5f-9b2c-6e170ac87aaf"
NF_0_UUID = "d5cb974d-2305-4eed-9e68-76031b1bd088"

# Initialize Firebase SDK
try:
    cred = credentials.Certificate("./ServiceAccountKey.json")
    default_app = firebase_admin.initialize_app(cred)
    db = firestore.client()
except:
    print("StockX-product-fetch.py error: could not connect to Firebase.")


def fetch_info(uuid, brand, item):
# Make RESTFUL API query 

    #Fetch 
    BASE_URL = "https://stockx.com/api/products/" + uuid 
    URL = BASE_URL + "?includes=market"
    HEADERS = {"x-api-key":"stockxheartspartahack"}


    r = requests.get(URL, headers=HEADERS)
    data = r.json()

    #Temporary fix for missing 'retailPrice' field
    if (brand=='supreme'):
        retailPrice = '498'
    else:
        retailPrice = str(data['Product']['retailPrice'])
    name = str(data['Product']['title'])
    releaseDate = str(data['Product']['releaseDate'])
    imageUrl = str(data['Product']['media']['smallImageUrl'])
    high = str(data['Product']['market']['annualHigh'])
    low = str(data['Product']['market']['annualLow'])

    
    #For debugging
    # print(retailPrice)
    # print(name)
    # print(releaseDate)
    # print(imageUrl)
    # print(high)
    # print(low)


    # Send all categories to Firebase in one batch upload
    batch = db.batch()
    doc_ref = db.collection(brand).document(item)
    batch.set(doc_ref, {
        u'retailPrice': retailPrice,
        u'name': name,
        u'releaseDate': releaseDate,
        u'imageUrl': imageUrl,
        u'high': high,
        u'low': low,
    })
    # Commit the batch and send all the cards to Firebase at once
    batch.commit()
    
    print("Successfully written StockX product data!")

    return
    

# Invoke product data fetch and storage
fetch_info(YEEZY_0_UUID,'adidas','yeezy_0')
fetch_info(JORDAN_0_UUID, 'nike', 'jordan_0')
fetch_info(NF_0_UUID, 'supreme', 'jacket_0')


    