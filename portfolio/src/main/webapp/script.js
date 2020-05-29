// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.


/* Scroll to the section selected */
function scrollToDiv(divName) {
    var elemDiv = document.getElementById(divName);
    elemDiv.scrollIntoView({ behavior: 'smooth', block: 'center' });
}

/* Add fade in effect to reveal post */
function fadeIn(divName) { 
    scrollToDiv(divName);
    var elemDiv = document.getElementById(divName);
    var opacityVal = parseFloat(elemDiv.style.opacity);
    
    if(divName != 'home') {
        // Set invisible the other post
        if(divName == 'whycs') {
            setInvisible('step');
        }
        else {
            setInvisible('whycs');
        }
    }

    var timer = setInterval(function () { 
        if(opacityVal >= 1.0)
            clearInterval(timer);
        opacityVal += 0.1;
        elemDiv.style.opacity= opacityVal;
    }, 100)

}

/* Makde post invisible */
function setInvisible(divName) {
    var elemDiv = document.getElementById(divName);
    elemDiv.style.opacity = 0;    
}