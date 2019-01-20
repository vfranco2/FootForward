#Use a linear regression model to predict the next price point 30 days from now
import csv
import numpy as np
from sklearn.svm import SVR
import matplotlib.pyplot as plt
import firebase_admin
from firebase_admin import credentials, firestore

# Initialize Firebase SDK
try:
    cred = credentials.Certificate("./ServiceAccountKey.json")
    default_app = firebase_admin.initialize_app(cred)
    db = firestore.client()
except:
    print("predictor.py error: could not connect to Firebase.")

############################
# Initialize two empty lists
dates = []
prices = []
# Pass prediction day for end-of-month to the SVM for prediction
predictionDay = 30

def get_data(filename):
    '''
    Reads data from a file (xxxx.csv) and adds data to
    the lists: dates and prices
    '''
	# Reset the data lists upon function call
    dates = []
    prices = []
    # Use the with as block to open the file and assign it to csvfile
    with open(filename, 'r') as csvfile:
        # csvFileReader allows us to iterate over every row in our csv file
        csvFileReader = csv.reader(csvfile)
        next(csvFileReader)    # skipping column names
        for row in csvFileReader:
            dates.append(int(row[0].split('-')[0])) # Only gets day of the month which is at index 0
            prices.append(float(row[1])) # Convert to float for more precision


    return dates, prices


def predict_price(dates, prices, x):
    '''
    Builds predictive model and graphs it
    This function creates 3 models, each of them will be a type of support vector machine.
    A support vector machine is a linear seperator. It takes data that is already classified and tries
    to predict a set of unclassified data.
    So if we only had two data classes it would look like this
    It will be such that the distances from the closest points in each of the two groups is farthest away.
    When we add a new data point in our graph depending on which side of the line it is we could classify it
    accordingly with the label. However, in this program we are not predicting a class label, so we don't
    need to classify instead we are predicting the next value in a series which means we want to use regression.
    SVM's can be used for regression as well. The support vector regression is a type of SVM that uses the space between
    data points as a margin of error and predicts the most likely next point in a dataset.
    The predict_prices returns predictions from each of our models
    '''
    
    dates = np.reshape(dates,(len(dates), 1)) # converting to matrix of n X 1

    # Linear support vector regression model.
    # Takes in 3 parameters:
    #     1. kernel: type of svm
    #     2. C: penalty parameter of the error term
    #     3. gamma: defines how far too far is.

    # Two things are required when using an SVR, a line with the largest minimum margin
    # and a line that correctly seperates as many instances as possible. Since we can't have both,
    # C determines how much we want the latter.

    # Next we make a polynomial SVR because in mathfolklore, the no free lunch theorum states that there are no guarantees for one optimization to work better
    # than the other. So we'll try both.

    # Finally, we create one more SVR using a radial basis function. RBF defines similarity to be the eucledian distance between two inputs
    # If both are right on top of each other, the max similarity is one, if too far it is a ze
    svr_lin = SVR(kernel= 'linear', C= 1e3) # 1e3 denotes 1000
    
    svr_lin.fit(dates, prices)

    # This plots the initial data points as black dots with the data label and plot
    # each of our models as well
    
    # The graphs are plotted with the help of SVR object in scikit-learn using the dates matrix as our parameter.
    # Each will be a distinct color and and give them a distinct label.
    # plt.scatter(dates, prices, color= 'black', label= 'Data') # plotting the initial datapoints 
    # plt.plot(dates,svr_lin.predict(dates), color= 'green', label= 'Linear model') # plotting the line made by linear kernel
    # plt.xlabel('Date') # Setting the x-axis
    # plt.ylabel('Price') # Setting the y-axis
    # plt.title('Support Vector Regression') # Setting title
    # plt.legend() # Add legend
    # plt.show() # To display result on screen
    X = np.sort(np.random.rand(predictionDay, 1), axis=0)


    return  svr_lin.predict(X)[0] # returns predictions from each of our models


def send_to_firebase(brand, item, pricePred):
    try:
        # Send all categories to Firebase in one batch upload
        doc_ref = db.collection(brand).document(item)
        doc_ref.set({
			u'priceForecast': str(int(pricePred))
		}, merge=True)
           
        print(item+": successfully written to Firebase!") 
    except:
        print(item+" ERROR: could not write to Firebase.")


#Compute price forecasts for end of the month and send to Firebase
dates, prices = get_data('yeezy_prices.csv') # calling get_data method by passing the csv file to it
yeezy_price_forecast = predict_price(dates, prices, predictionDay)
print('Predicted price for Yeezy Powerphase Calabasas Core White:', yeezy_price_forecast)
send_to_firebase('adidas','yeezy_0',yeezy_price_forecast)

dates, prices = get_data('jordan_prices.csv') # calling get_data method by passing the csv file to it
jordan_price_forecast = predict_price(dates, prices, predictionDay)
print('Predicted price for Jordan 11 Retro Space Jam (2016):', jordan_price_forecast)
send_to_firebase('nike','jordan_0',jordan_price_forecast)


dates, prices = get_data('nf_prices.csv') # calling get_data method by passing the csv file to it
nf_price_forecast = predict_price(dates, prices, predictionDay)
print('Predicted price for The North Face Mountain Baltoro Jacket Blue/White:', nf_price_forecast)
send_to_firebase('supreme','jacket_0',nf_price_forecast)

