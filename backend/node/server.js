const express = require("express");
const puppeteer = require("puppeteer");
const fs = require("fs");
const path = require("path");
const cors = require("cors");
const app = express();

app.use(cors());
app.use(express.json({ limit: '10mb' }));

app.use('/recursos', express.static(path.join(__dirname, 'recursos')));
app.use('/', express.static(path.join(__dirname, 'templates')));

app.post("/generar-ticket-cita", async (req, res) => {
  try {
    const {
      cliente,
      dni,
      fecha,
      hora,
      numeroCita,
      medico,
      especialidad,
      metodoPago,
      monto
    } = req.body;

    const templatePath = path.join(__dirname, "templates", "ticket-cita.html");
    let template = fs.readFileSync(templatePath, "utf8");

    template = template
      .replace("{{cliente}}", cliente)
      .replace("{{dni}}", dni)
      .replace("{{fecha}}", fecha)
      .replace("{{hora}}", hora)
      .replace("{{numeroCita}}", numeroCita)
      .replace("{{medico}}", medico)
      .replace("{{especialidad}}", especialidad)
      .replace("{{metodoPago}}", metodoPago)
      .replace("{{monto}}", monto.toFixed(2));

    const browser = await puppeteer.launch({
      args: ["--no-sandbox"],
      headless: "new"
    });

    const page = await browser.newPage();
    await page.setContent(template, { waitUntil: "networkidle0" });

    const pdf = await page.pdf({
      format: "A4",
      printBackground: true
    });

    await browser.close();

    res.setHeader("Content-Type", "application/pdf");
    res.send(pdf);

  } catch (err) {
    res.status(500).json({ error: err.message });
  }
});

app.post("/generar-receta", async (req, res) => {
  try {
    const {
      paciente,
      dni,
      edad,
      medico,
      especialidad,
      diagnostico,
      receta,
      indicaciones,
      fecha
    } = req.body;

    const templatePath = path.join(__dirname, "templates", "receta.html");
    let template = fs.readFileSync(templatePath, "utf8");

    template = template
      .replace("{{paciente}}", paciente)
      .replace("{{dni}}", dni)
      .replace("{{edad}}", edad)
      .replace("{{medico}}", medico)
      .replace("{{especialidad}}", especialidad)
      .replace("{{diagnostico}}", diagnostico)
      .replace("{{receta}}", receta)
      .replace("{{indicaciones}}", indicaciones)
      .replace("{{fecha}}", fecha);

    const browser = await puppeteer.launch({
      args: ["--no-sandbox"],
      headless: "new"
    });

    const page = await browser.newPage();
    await page.setContent(template, { waitUntil: "networkidle0" });

    const pdf = await page.pdf({
      format: "A4",
      printBackground: true
    });

    await browser.close();

    res.setHeader("Content-Type", "application/pdf");
    res.send(pdf);

  } catch (err) {
    res.status(500).json({ error: err.message });
  }
});


app.listen(3005, () => {
  console.log("CitaMed PDF Service running on port 3005");
});
