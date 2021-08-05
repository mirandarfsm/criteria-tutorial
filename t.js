function techList(arrOfTechs, name) {
    if (arrOfTechs.length === 0) {
        return 'Vazio!';
    }

    let list = [];
    
    arrOfTechs.sort();
    for (let tech of arrOfTechs) {
        let objectList = new Object();
        objectList['tech'] = tech;
        objectList['name'] = name;
        list.push(objectList);
    }

    return list;
}
console.log(techList(["React", "Jest", "HTML", "CSS", "JavaScript"], "Lucas"));