
    let toDoForm = document.querySelector(".toDo-form-addTask");
    let toDoList = document.querySelector(".toDo-list");
function addTask1(text) {
    //element todo
    const todo = document.createElement('div');
    todo.classList.add('toDo-element');

    //belka gorna
    const todoBar = document.createElement('div');
    todoBar.classList.add('toDo-element-bar');

    //data w belce
    const todoDate = document.createElement('div');
    todoDate.classList.add('toDo-element-bar');
    const date = new Date();
    const dateText = date.getDate() + '-' + (date.getMonth()+1) + '-' + date.getFullYear() + ' godz.: ' + date.getHours() + ':' + date.getMinutes();
    todoDate.innerText = dateText;

    //przycisk usuwania
    const todoDelete = document.createElement('button');
    todoDelete.classList.add('toDo-element-deleteButton');
    
    todoDelete.innerHTML = 'x';

    //wrzucamy elementy do belki
    todoBar.appendChild(todoDate);
    todoBar.appendChild(todoDelete);

    //element z tekstem
    const todoText = document.createElement('div');
    todoText.classList.add('toDo-element-text');
    todoText.innerText = text;

    //laczymy calosc
    todo.appendChild(todoBar);
    todo.appendChild(todoText);

    //i wrzucamy do listy
    toDoList.append(todo);
}
function addTask(text)
{
    const toDoElement = document.createElement("div");
    toDoElement.classList.add("toDo-element");
    
    const toDoBar = document.createElement("div");
    toDoBar.classList.add("toDo-element-bar");
    
    const h3 = document.createElement("h3");
    h3.classList.add("toDo-elementDate");
    const date = new Date();
    h3.innerText=date.getFullYear()+"/"+ date.getMonth()+"/"+(date.getDay()+1)+" "+date.getHours()+":"+date.getMinutes()+":"+date.getSeconds();
    
    const button = document.createElement("button");
    button.classList.add("toDo-element-deleteButton");
    button.innerText = "X";
    
    toDoBar.appendChild(h3);
    toDoBar.appendChild(button);
    
    const toDoElementText=document.createElement("div");
    toDoElementText.classList.add("toDo-element-text");
    toDoElementText.innerText=text;
    
    toDoElement.appendChild(toDoBar);
    toDoElement.appendChild(toDoElementText);
    
    toDoList.append(toDoElement);
}

window.onload = function()
{
    toDoForm = document.querySelector(".toDo-form-addTask");
    toDoList = document.querySelector(".toDo-list");
    
    
    
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

      
    toDoList.addEventListener("click",function(e)
     {
         
        if (e.target.closest(".toDo-element-deleteButton")!==null)
        {
            e.target.closest(".toDo-element").remove();
        }
        
     });
    

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