import React, { Component } from "react";
import { Document, Page, pdfjs } from "react-pdf";
pdfjs.GlobalWorkerOptions.workerSrc = `//cdnjs.cloudflare.com/ajax/libs/pdf.js/${pdfjs.version}/pdf.worker.js`;

export class PdfReader extends Component {
  state = {
    file: this.props.document.file,
    numPages: null,
    pageNumber: 1
  };

  onDocumentLoadSuccess = ({ numPages }) => {
    this.setState({ numPages });
  };

  goToPrevPage = () =>
    this.setState(state => ({ pageNumber: state.pageNumber - 1 }));
  goToNextPage = () =>
    this.setState(state => ({ pageNumber: state.pageNumber + 1 }));


    // loads before render function
    componentWillReceiveProps(props) {
        this.setState({
          pageNumber: props.document.currPage
        });
    }


  render() {
    const { file, pageNumber, numPages } = this.state;

    return (
      <div>
        <nav>
          <button onClick={this.goToPrevPage}>Prev</button>
          <button onClick={this.goToNextPage}>Next</button>
        </nav>

        <div style={{ width: 300 }}>
          <Document
            file={file}
            onLoadSuccess={this.onDocumentLoadSuccess}
          >
            <Page pageNumber={pageNumber} width={600} />
          </Document>
        </div>

        <p>
          Page {pageNumber} of {numPages}
        </p>
      </div>
    );
  }
}
