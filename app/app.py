import os

import cf_deployment_tracker
from flask import Flask, render_template
from werkzeug.contrib.fixers import ProxyFix

# Emit Bluemix deployment event
cf_deployment_tracker.track()

app = Flask(__name__)


@app.route('/')
def home():
    return render_template('index.html')


app.wsgi_app = ProxyFix(app.wsgi_app)
if __name__ == '__main__':
    app.run()
