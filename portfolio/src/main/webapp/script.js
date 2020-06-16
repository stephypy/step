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

function pageLoad() {
  fadeIn('home');
  addCommentsToDom();
  const openCommentsButton = document.getElementById('open-comments');
  openCommentsButton.addEventListener('click', openComments);
  const chartsLink = document.getElementById('charts-link');
  chartsLink.addEventListener('click', goToCharts);
}
window.onload = pageLoad;

function addCommentsToDom() {
  fetch('/data')
    .then((response) => response.json())
    .then((comments) => {
      const commentsSection = document.getElementById('whycs-comments');
      if (comments.length > 0) {
        comments.forEach((comment) => {
          commentsSection.appendChild(
            createListElement(comment.nickname, 'nickname')
          );
          commentsSection.appendChild(
            createListElement(comment.commentContent, 'comments')
          );
        });
      }
    });
}

function createListElement(text, className) {
  const liElement = document.createElement('li');
  const liContent = document.createTextNode(text);
  liElement.className = className;
  liElement.appendChild(liContent);
  return liElement;
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

function goToCharts() {
  window.location.href = '/charts.html';
}
