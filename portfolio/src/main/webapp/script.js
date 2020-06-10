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

window.addEventListener("load", addCommentsToDom)
function addCommentsToDom() {    
    fetch('/data').then(response => response.json()).then((comments) => {
        const commentsSection = document.getElementById('individual-comments');
        if(comments.length > 0) {
            comments.forEach((comment) => {
                commentsSection.appendChild(createListElement(comment.nickname + ":"));
                commentsSection.appendChild(createListElement(comment.commentContent + " ", comment.sentimentScore));
            });    
        }
    });
}

function createListElement(text, score = 2) {
    // Thumbs up emoji
    const positiveEmoji = "&#128077";
    // Grimacing emoji
    const negativeEmoji = "&#128556";
    // Bubble speech emoji
    const neutralEmoji = "&#128172";

    if(score <= -0.6) {
      text += negativeEmoji;
    }
    else if(score >= 0.6) {
      text += positiveEmoji;
    }
    else {
      text += neutralEmoji;
    }
    
    const liElement = document.createElement('li');
    var liContent = document.createTextNode(text);
    liElement.appendChild(liContent);
    return liElement;
}

/* Scroll to the section selected */
function scrollToDiv(divName) {
    var elemDiv = document.getElementById(divName);
    elemDiv.scrollIntoView({ behavior: 'smooth', block: 'center' });
}

/* Add fade in effect to reveal post */
function fadeIn(divName) { 
    scrollToDiv(divName);
    var elemDiv = document.getElementById(divName);
    elemDiv.style.visibility = "visible";
    var opacityVal = parseFloat(elemDiv.style.opacity);
    
    // Set invisible the other post
    if(divName == 'home') {
        setInvisible('whycs');
        setInvisible('step');
    }
    else if(divName == 'whycs') {
        setInvisible('step');        
    }
    else {
        setInvisible('whycs');
    }
    

    var timer = setInterval(function () { 
        if(opacityVal >= 1.0)
            clearInterval(timer);
        opacityVal += 0.1;
        elemDiv.style.opacity= Math.min(1, opacityVal);
    }, 100)
}

/* Make post invisible */
function setInvisible(divName) {
    var elemDiv = document.getElementById(divName);
    elemDiv.style.opacity = 0; 
    elemDiv.style.visibility = "hidden";   
}