# Fetch JSON stock historical data from StockX API and convert to .csv file
import requests
import datetime
import time
import csv

# Test product retrieval
YEEZY_0_UUID = "1a277efe-4aa1-41d2-b33a-4a08b256b046"
JORDAN_0_UUID = "5ee9da0d-d10a-4d5f-9b2c-6e170ac87aaf"
NF_0_UUID = "d5cb974d-2305-4eed-9e68-76031b1bd088"

yeezy_file = "yeezy_prices.csv"
jordan_file = "jordan_prices.csv"
nf_file = "nf_prices.csv"

# Function to fetch historical average product prices from StockX API
def fetch_product_price_data(uuid, csvfile):
    #Fetch average daily price data, starting from June 2017 to Jan of 2019
    BASE_URL = "https://gateway.stockx.com/public/v1/products/" + uuid 
    URL = BASE_URL + "/chart?start_date=2019-01-01&end_date=2019-01-19&intervals=100&format=highstock&currency=USD"
    HEADERS = {"x-api-key":"stockxheartspartahack"}
    pricesList = [] #total list of daily sale price averages
    daysList = []  #days of the month indexed from 0

    r = requests.get(URL, headers=HEADERS)

    #1) Parse returned json and grab price averages for first date range
    data = r.json()
    sales = data['series'][0]['data']
    for sale in sales:
        #print(datetime.datetime.fromtimestamp(sale[0]/1000).strftime('%d-%Y-%m'), sale[1]) #trim trailing zeros and convert timestamp to date
        daysList.append(datetime.datetime.fromtimestamp(sale[0]/1000).strftime('%d-%Y-%m'))
        pricesList.append(sale[1]) #add the average price for each day to salesList


    # Write a list of values to a .csv
    with open(csvfile, "w") as output:
        writer = csv.writer(output, lineterminator='\n')
        writer.writerow(['Date','Price'])
        for i in range(len(pricesList)):
            writer.writerow([daysList[i],pricesList[i]])  
    
    return 



#Fetch and write product average prices to .csv files
##################################################

fetch_product_price_data(YEEZY_0_UUID, yeezy_file)
fetch_product_price_data(JORDAN_0_UUID, jordan_file)
fetch_product_price_data(NF_0_UUID, nf_file)





