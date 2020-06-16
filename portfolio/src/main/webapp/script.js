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

/* Functions to be called when homepage gets loaded */
function pageLoad() {
  fadeIn('home');
  addCommentsToDom();
  const openCommentsButton = document.getElementById('open-comments');
  openCommentsButton.addEventListener('click', openComments);
}
window.onload = pageLoad;

/* Fetch comments to be added to page */
function addCommentsToDom() {
  fetch('/data').then((response) => response.json()).then((comments) => {
      const commentsSection = document.getElementById('whycs-comments');
      if (comments.error) {
        return;
      }
      comments.forEach((comment) => {
        commentsSection.appendChild(createUsernameElem(comment.nickname));
        commentsSection.appendChild(createSentimentCommentElem(comment.content, comment.sentimentScore));
      });
  });
}

/* Create the username element of the comment */
function createUsernameElem(username) {
  const dtElement = document.createElement('dt');
  const dtContent = document.createTextNode(username);

  dtElement.className = 'nickname';
  dtElement.appendChild(dtContent);

  return dtElement;
}

/* Create the content of the element of the comment 
and parse  an emoji  appropiate to sentiment score */
function createSentimentCommentElem(content, sentimentScore) {
  const positiveEmoji = ' &#128516';
  const neutralEmoji = ' &#128172';
  const negativeEmoji = ' &#128556';

  const ddElement = document.createElement('dd');
  const ddSentiment = document.createElement('span');
  const ddContent = document.createTextNode(content);

  ddElement.className = 'comments';
  ddElement.appendChild(ddContent);

  if (sentimentScore <= -0.6) {
    ddSentiment.innerHTML = negativeEmoji;
  } else if (sentimentScore >= 0.6) {
    ddSentiment.innerHTML = positiveEmoji;
  } else {
    ddSentiment.innerHTML = neutralEmoji;
  }
  
  ddElement.appendChild(ddSentiment);
  return ddElement;
}


/* Open the modal box with comments */
function openComments() {
  document.getElementById('whycs-modal').style.display = 'block';
}

/* Close modal when the X symbol is clicked or when user clicks outside the modal content */
window.onclick = function (evt) {
  if (
    evt.target == document.getElementById('whycs-modal') ||
    evt.target == document.getElementById('whycs-close')
  ) {
    document.getElementById('whycs-modal').style.display = 'none';
  }
};

/* Scroll to the section selected */
function scrollToDiv(divName) {
  const elemDiv = document.getElementById(divName);
  elemDiv.scrollIntoView({ behavior: 'smooth', block: 'center' });
}

/* Add fade in effect to reveal post */
function fadeIn(divName) {
  scrollToDiv(divName);
  const elemDiv = document.getElementById(divName);
  elemDiv.style.visibility = 'visible';
  let opacityVal = parseFloat(elemDiv.style.opacity);

  // Set invisible the other post
  if (divName == 'home') {
    setInvisible('whycs');
    setInvisible('step');
  } else if (divName == 'whycs') {
    setInvisible('step');
  } else {
    setInvisible('whycs');
  }

  const timer = setInterval(function () {
    if (opacityVal >= 1.0) clearInterval(timer);
    opacityVal += 0.1;
    elemDiv.style.opacity = Math.min(1, opacityVal);
  }, 100);
}

/* Make post invisible */
function setInvisible(divName) {
  const elemDiv = document.getElementById(divName);
  elemDiv.style.opacity = 0;
  elemDiv.style.visibility = 'hidden';
}
