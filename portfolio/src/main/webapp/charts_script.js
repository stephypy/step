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

/* Go to homepage */
function goToHomePage() {
  window.location.href = '/';
}
const goBackButton = document.getElementById('go-back');
goBackButton.addEventListener('click', goToHomePage);

/* global google */
google.charts.load('current', { packages: ['corechart'] });
google.charts.setOnLoadCallback(drawChart);

/** Creates a chart and adds it to the page. */
function drawChart() {
  fetch('/hotdog')
    .then((response) => response.json())
    .then((allVotes) => {
      const data = new google.visualization.DataTable();
      data.addColumn('string', 'answer');
      data.addColumn('number', 'count');
      Object.keys(allVotes).forEach((vote) => {
        data.addRow([vote, allVotes[vote]]);
      });

      const options = {
        width: 500,
        height: 400,
      };

      const chart = new google.visualization.PieChart(
        document.getElementById('pie-chart')
      );
      chart.draw(data, options);
    });
}
