
    let toDoForm = document.querySelector(".toDo-form-addTask");
    let toDoList = document.querySelector(".toDo-list");

function addTask(text)
{
    // glowny element
    const toDoElement = document.createElement("div");
    toDoElement.classList.add("toDo-element");
    
    //pasek elementu
    const toDoBar = document.createElement("div");
    toDoBar.classList.add("toDo-element-bar");
    
    //data 
    const h3 = document.createElement("h3");
    h3.classList.add("toDo-elementDate");
    const date = new Date();
    h3.innerText=date.getFullYear()+"/"+ date.getMonth()+"/"+(date.getDay()+1)+" "+date.getHours()+":"+date.getMinutes()+":"+date.getSeconds();
    
    //przycisk do usuwania
    const button = document.createElement("button");
    button.classList.add("toDo-element-deleteButton");
    button.innerText = "X";
    
    //dodawanie przycisku i daty do paska
    toDoBar.appendChild(h3);
    toDoBar.appendChild(button);
    
    //element z tekstem zadania
    const toDoElementText=document.createElement("div");
    toDoElementText.classList.add("toDo-element-text");
    toDoElementText.innerText=text;
    
    toDoElement.appendChild(toDoBar);
    toDoElement.appendChild(toDoElementText);
    
    //dodanie do listy zadania
    toDoList.append(toDoElement);
}

window.onload = function()
{
    toDoForm = document.querySelector(".toDo-form-addTask");
    toDoList = document.querySelector(".toDo-list");
    
    
    //listener odpowiedzialny za dodawanie zadan
    toDoForm.addEventListener("submit",function(e)
        {
            e.preventDefault();
            const textArea=this.querySelector(".toDo-form-textArea");
            if (textArea.value!=="")
            {
                
                addTask(textArea.value);
                textArea.value="";
            }
            
        }
    );

    //listener odpowiedzialny za usuwanie zadan
    toDoList.addEventListener("click",function(e)
     {
         
        if (e.target.closest(".toDo-element-deleteButton")!==null)
        {
            e.target.closest(".toDo-element").remove();
        }
        
     });
    
    // listener odpowiedzialny za wyszukiwanie zadan
    var toDoSearch = document.querySelector("#toDo-input-search");
    toDoSearch.addEventListener("input",function()
    {
            var current = this.value;
            var list = document.querySelectorAll(".toDo-element");
            
            [].forEach.call(list, function(el)
            {
                var text = el.querySelector(".toDo-element-text").innerText;
                if(text.indexOf(current)!==-1)
                {
                    el.style.setProperty("display","");
                }
                else
                {
                    el.style.setProperty("display","none");
                }
            });
        
        
    });
    
};