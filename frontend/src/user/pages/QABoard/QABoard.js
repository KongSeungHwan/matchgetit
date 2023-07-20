import React, { useState, useEffect } from 'react';
import Pagination from './js/Pagination';
import './css/QABoard.css'
import QARegisterForm from "./QARegisterForm";

function QABoard({session}) {
  const [tableData, setTableData] = useState([]);
  const [comment, setComment] = useState([]);
  const [currentPage, setCurrentPage] = useState(1);
  const [itemsPerPage, setItemsPerPage] = useState(10);
  const [showQABoardInquiry, setShowQABoardInquiry] = useState(false);
  const [expandedRowIndex, setExpandedRowIndex] = useState(-1);

  const [commentInput, setCommentInput] = useState('');
  const [comments, setComments] = useState([]);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const response = await fetch('/matchGetIt/userInquiryList');
        const data = await response.json();
        console.log(data);
        if (Array.isArray(data)) {
          setTableData(data);
        } else {
          setTableData([]);
        }
      } catch (error) {
        console.error('Error fetching data:', error);
      }
    };
    fetchData();
  }, []);

  useEffect(() => {
    const fetchData2 = async () => {
      try {
        const response2 = await fetch('/matchGetIt/userInquiryCommentList');
        const commentData = await response2.json();
        if (Array.isArray(commentData)) {
          setComment(commentData);
        } else {
          setComment([]);
        }
      } catch (error) {
        console.error('Error fetching data:', error);
      }
    };
    fetchData2();
  }, []);

  const getCurrentItems = () => {
    const startIndex = (currentPage - 1) * itemsPerPage;
    const endIndex = startIndex + itemsPerPage;
    return tableData.slice(startIndex, endIndex);
  };

  const totalItems = tableData.length;
  const totalPages = Math.ceil(totalItems / itemsPerPage);
  const currentItems = getCurrentItems();

  const handlePageChange = (pageNumber) => {
    setCurrentPage(pageNumber);
  };

  const handleRegisterClick = () => {
    setShowQABoardInquiry(true)
  };

  const handleBackClick = () => {
    setShowQABoardInquiry(false);
  };

  const formatDateTime = (dateTimeString) => {
    const dateTime = new Date(dateTimeString);
    return dateTime.toLocaleString('en-US', {
      year: 'numeric',
      month: '2-digit',
      day: '2-digit'
      //     ,hour: '2-digit',
      // minute: '2-digit',
    });
  };

  const handleRowClick = (index) => {
    if (expandedRowIndex === index) {
      setExpandedRowIndex(-1);
    } else {
      setExpandedRowIndex(index);
    }
  };

  // const handleCommentInputChange = (event) => {
  //   setCommentInput(event.target.value);
  // };
  //
  // const handleCommentSubmit = (event, index) => {
  //   event.preventDefault();
  //   if (commentInput.trim() === '') {
  //     return;
  //   }
  //   const newComment = {
  //     index: index,
  //     content: commentInput,
  //   };
  //   setComments((prevComments) => [...prevComments, newComment]);
  //   setCommentInput('');
  // };

  const getCommentsByInquiryId = (inquiryId) => {
    return comment.filter((commentItem) => commentItem.inquiryId === inquiryId);
  };


  return (
    <div className="bulletin-board">
      <div className="QABoardName">
        <a>Q & A</a>
      </div>
      <fieldset>
      {!showQABoardInquiry ? (
      <div className="QABoardContent">
        <table className="QABoardTable">
          <thead>
          <tr>
            <th className="QATitle">제목</th>
            <th className="QAWriter">작성자</th>
            <th className="QADate">날짜</th>
          </tr>
          </thead>
          <tbody>
          {currentItems.map((data, index) => (
              <React.Fragment key={index}>
                <tr onClick={() => handleRowClick(index)}>
                  <td>{data.title}</td>
                  <td>{data.createdBy}</td>
                  <td>{formatDateTime(data.regTime)}</td>
              </tr>
                {expandedRowIndex === index && (
                    <tr>
                      <td colSpan="3">
                        <div className="QABoardContent">{data.content}</div>
                        <hr />
                        <div className="QABoardCommentdiv">
                          {getCommentsByInquiryId(data.inquiryId).map((commentItem, commentIndex)=>(
                              <div key={commentIndex} className="QABoardComment">
                                <p>관리자 : {commentItem.content}</p>
                              </div>
                          ))}
                        </div>
                        {/*<form onSubmit={(event) => handleCommentSubmit(event, index)}>*/}
                        {/*  <input type="text" value={commentInput} onChange={handleCommentInputChange} />*/}
                        {/*  <button type="submit">댓글 작성</button>*/}
                        {/*</form>*/}

                        {/*<ul>*/}
                        {/*  {comments*/}
                        {/*      .filter((comment) => comment.index === index)*/}
                        {/*      .map((comment, commentIndex) => (*/}
                        {/*          <li key={commentIndex}>{session.name} | {comment.content}</li>*/}
                        {/*      ))}*/}
                        {/*</ul>*/}
                      </td>
                    </tr>
                )}
              </React.Fragment>
          ))}
          </tbody>
        </table>
        <div className="QABtnDiv">
          <button className="QABtn" onClick={handleRegisterClick}>
            등록하기
          </button>
        </div>
      </div>
      ) : (
          <QARegisterForm session={session} onBackClick={handleBackClick} />
      )}
      </fieldset>

      <div className="QABoardPaging">
      <Pagination
        totalItems={totalItems}
        itemsPerPage={itemsPerPage}
        currentPage={currentPage}
        onPageChange={handlePageChange}
      />
      </div>
    </div>
  );
}

export default QABoard;
