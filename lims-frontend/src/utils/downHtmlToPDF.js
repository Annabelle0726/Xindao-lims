import html2canvas from "html2canvas";
import jsPDF from "jspdf";

export async function exportHtmlToPDF(element, name = "exported") {
  try {
    // 将 HTML 元素转换为 canvas
    console.log("正在将 HTML 转换为 canvas...", element);
    const canvas = await html2canvas(element, { useCORS: true });
    const imgData = canvas.toDataURL("image/png");

    // 创建 PDF
    const pdf = new jsPDF("p", "mm", "a4");
    const pdfWidth = pdf.internal.pageSize.getWidth();
    const pdfHeight = (canvas.height * pdfWidth) / canvas.width;

    pdf.addImage(imgData, "PNG", 10, 10, pdfWidth - 20, pdfHeight - 20);
    pdf.save(name + ".pdf");
    console.log("PDF 导出成功！");
  } catch (error) {
    console.error("导出 PDF 失败：", error);
  }
}
