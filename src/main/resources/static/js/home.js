const inputArquivo = document.getElementById("arquivo");
const botaoEnviar = document.getElementById("enviar");

botaoEnviar.addEventListener("click", async() =>{
    const arquivo = inputArquivo.files[0];

    if(!arquivo){
        alert("Selecione um arquivo!");
        return;
    }

    const formData = new FormData();
    formData.append("file", arquivo);

    inputArquivo.value = "";
    console.log("Enviando arquivo");
    const resposta = await fetch("/api/pdf/gerar-Cards", {
        method: "POST",
        body: formData
    });

    console.log("Arquivo recebido e analisado");
    const flashcards = await resposta.json();

    console.log(flashcards);
});