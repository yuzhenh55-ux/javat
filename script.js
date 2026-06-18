let records =
JSON.parse(
localStorage.getItem("healthLogs")
) || [];

document.getElementById("date").value =
new Date().toISOString().split("T")[0];

loadTable();

function calculateRisk(
sleep,
steps,
mood
){

    if(sleep < 6){

        if(steps < 4000){

            if(mood < 5){

                return "HIGH";
            }

            return "MEDIUM";
        }

        return "MEDIUM";
    }

    if(
        steps > 7000 &&
        mood > 6
    ){

        return "LOW";
    }

    return "MEDIUM";
}

function addRecord(){

    let date =
    document.getElementById(
        "date"
    ).value;

    let sleep =
    Number(
        document.getElementById(
            "sleep"
        ).value
    );

    let steps =
    Number(
        document.getElementById(
            "steps"
        ).value
    );

    let mood =
    Number(
        document.getElementById(
            "mood"
        ).value
    );

    let risk =
    calculateRisk(
        sleep,
        steps,
        mood
    );

    document.getElementById(
        "riskLabel"
    ).innerHTML =
    "風險：" + risk;

    records.push({
        date,
        sleep,
        steps,
        mood,
        risk
    });

    localStorage.setItem(
        "healthLogs",
        JSON.stringify(records)
    );

    loadTable();
}

function loadTable(){

    let body =
    document.getElementById(
        "tableBody"
    );

    body.innerHTML="";

    records.forEach(r=>{

        body.innerHTML += `
        <tr>
            <td>${r.date}</td>
            <td>${r.sleep}</td>
            <td>${r.steps}</td>
            <td>${r.mood}</td>
            <td>${r.risk}</td>
        </tr>`;
    });
}

function showTrend(){

    let total =
    records.length;

    if(total===0){

        document.getElementById(
            "trendArea"
        ).innerHTML =
        "目前沒有資料";

        return;
    }

    let sleep=0;
    let steps=0;
    let mood=0;

    let low=0;
    let medium=0;
    let high=0;

    records.forEach(r=>{

        sleep += r.sleep;
        steps += r.steps;
        mood += r.mood;

        if(r.risk==="LOW") low++;
        else if(r.risk==="MEDIUM") medium++;
        else high++;
    });

    document.getElementById(
        "trendArea"
    ).innerHTML =

    `
    <p>總紀錄數：${total}</p>

    <p>平均睡眠：
    ${(sleep/total).toFixed(2)} 小時</p>

    <p>平均步數：
    ${(steps/total).toFixed(0)} 步</p>

    <p>平均心情：
    ${(mood/total).toFixed(2)}</p>

    <p>LOW：${low}</p>

    <p>MEDIUM：${medium}</p>

    <p>HIGH：${high}</p>
    `;
}
